package com.searchengine.service.impl;

import com.searchengine.dto.gitee.GiteeReadme;
import com.searchengine.dto.gitee.GiteeRepository;
import com.searchengine.entity.Repository;
import com.searchengine.mapper.RepositoryMapper;
import com.searchengine.service.GiteeOrgReposService;
import com.searchengine.service.GiteeRepoReadmeService;
import com.searchengine.service.GiteeRepositorySearchService;
import com.searchengine.service.RepositorySyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 仓库同步 Service 实现类（单线程，Impl 后缀，位于 impl 目录）。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RepositorySyncServiceImpl implements RepositorySyncService {

    private final GiteeRepositorySearchService giteeRepositorySearchService;
    private final GiteeOrgReposService giteeOrgReposService;
    private final GiteeRepoReadmeService giteeRepoReadmeService;
    private final RepositoryMapper repositoryMapper;

    @Override
    public List<Repository> syncByUsername(String username) {
        if (username == null || username.isBlank()) {
            return List.of();
        }
        String trimmed = username.trim();

        // 1. 先尝试按“用户”获取仓库（使用搜索仓库接口，以用户名作为关键词）
        List<GiteeRepository> repos = giteeRepositorySearchService.searchRepositories(trimmed);

        // 2. 若为空，认为是组织，再按组织获取仓库
        if (repos.isEmpty()) {
            repos = giteeOrgReposService.getOrgRepos(trimmed);
        }

        if (repos.isEmpty()) {
            log.info("未从 Gitee 获取到任何仓库：username={}", trimmed);
            return List.of();
        }

        // 3. 拉取 README，并构建本地 Repository 实体
        List<Repository> entities = new ArrayList<>(repos.size());
        for (GiteeRepository gr : repos) {
            Repository entity = new Repository();
            entity.setRepositoryId(gr.getId());
            entity.setFullName(gr.getFullName());
            entity.setHtmlUrl(gr.getHtmlUrl());

            // 根据 fullName 尝试解析 owner/repo；否则退回使用 username + 仓库名
            String owner = trimmed;
            String repoName = gr.getName();
            String fullName = gr.getFullName();
            if (fullName != null && fullName.contains("/")) {
                String[] parts = fullName.split("/", 2);
                if (parts.length == 2) {
                    owner = parts[0];
                    repoName = parts[1];
                }
            }

            // 调用 README 接口，若成功则填充 readme 字段
            GiteeReadme readme = giteeRepoReadmeService.getReadme(owner, repoName);
            if (readme != null) {
                entity.setReadme(readme.getContent());
            }

            entities.add(entity);
        }

        // 4. 单线程批量保存到 repository 表（使用自定义 batchInsert）
        if (!entities.isEmpty()) {
            try {
                repositoryMapper.batchInsert(entities);
            } catch (Exception e) {
                log.warn("批量插入仓库失败，考虑回退为逐条插入：size={}, error={}", entities.size(), e.getMessage(), e);
                // 降级为 MyBatis-Plus 单条插入
                for (Repository entity : entities) {
                    repositoryMapper.insertRepository(entity);
                }
            }
        }

        return entities;
    }
}

