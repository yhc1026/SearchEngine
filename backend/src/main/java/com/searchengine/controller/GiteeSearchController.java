package com.searchengine.controller;

import com.searchengine.dto.gitee.GiteeReadme;
import com.searchengine.dto.gitee.GiteeRepository;
import com.searchengine.dto.gitee.GiteeUser;
import com.searchengine.entity.Repository;
import com.searchengine.service.GiteeOrgReposService;
import com.searchengine.service.GiteeRepoReadmeService;
import com.searchengine.service.GiteeRepositorySearchService;
import com.searchengine.service.GiteeUserSearchService;
import com.searchengine.service.RepositorySyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Gitee 搜索接口：用户搜索、仓库搜索。
 */
@RestController
@RequestMapping("/api/gitee")
@RequiredArgsConstructor
public class GiteeSearchController {

    private final GiteeUserSearchService giteeUserSearchService;
    private final GiteeRepositorySearchService giteeRepositorySearchService;
    private final GiteeOrgReposService giteeOrgReposService;
    private final GiteeRepoReadmeService giteeRepoReadmeService;
    private final RepositorySyncService repositorySyncService;

    /**
     * 搜索用户
     *
     * @param q 关键词（必填）
     * @return 用户列表
     */
    @GetMapping("/users/search")
    public List<GiteeUser> searchUsers(@RequestParam("q") String q) {
        return giteeUserSearchService.searchUsers(q);
    }

    /**
     * 搜索仓库（入参用户名/关键词，出参仓库对象列表）
     *
     * @param username 用户名或搜索关键词（必填）
     * @return 仓库列表
     */
    @GetMapping("/repositories/search")
    public List<GiteeRepository> searchRepositories(@RequestParam("username") String username) {
        return giteeRepositorySearchService.searchRepositories(username);
    }

    /**
     * 按组织获取仓库列表（GET /api/v5/orgs/{org}/repos）
     *
     * @param org 组织 path（必填）
     * @return 仓库列表
     */
    @GetMapping("/orgs/{org}/repos")
    public List<GiteeRepository> getOrgRepos(@PathVariable("org") String org) {
        return giteeOrgReposService.getOrgRepos(org);
    }

    /**
     * 获取仓库 README（入参：仓库所有者 name + 仓库名字）
     *
     * @param ownerName 仓库所有者/组织 name（path）
     * @param repoName  仓库名字（path）
     * @return README 对象，无则 null
     */
    @GetMapping("/repos/readme")
    public GiteeReadme getRepoReadme(
            @RequestParam("ownerName") String ownerName,
            @RequestParam("repoName") String repoName) {
        return giteeRepoReadmeService.getReadme(ownerName, repoName);
    }

    /**
     * 获取仓库 README（路径形式，对应 GET /api/v5/repos/{owner}/{repo}/readme）
     *
     * @param owner 仓库所有者/组织 path（如 rulego）
     * @param repo  仓库名字（如 rulego）
     * @return README 对象，无则 null
     */
    @GetMapping("/repos/{owner}/{repo}/readme")
    public GiteeReadme getRepoReadmeByPath(
            @PathVariable("owner") String owner,
            @PathVariable("repo") String repo) {
        return giteeRepoReadmeService.getReadme(owner, repo);
    }

    /**
     * 按用户名同步仓库：
     * 1. 先按“用户”获取仓库；为空则按“组织”获取仓库；
     * 2. 为每个仓库获取 README 并填充；
     * 3. 单线程批量保存到 repository 表；
     * 4. 返回最终保存的仓库列表。
     */
    @GetMapping("/repositories/syncSearch")
    public List<Repository> syncRepositoriesByUsername(@RequestParam("username") String username) {
        return repositorySyncService.syncByUsername(username);
    }
}
