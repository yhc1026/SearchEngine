package com.searchengine.service.impl;

import com.searchengine.dto.gitee.GiteeRepository;

import java.util.List;

/**
 * Gitee 仓库搜索接口（Impl 后缀，位于 impl 目录）。
 * 实现类：{@link com.searchengine.service.GiteeRepositorySearchService}
 */
public interface GiteeRepositorySearchServiceImpl {

    List<GiteeRepository> searchRepositories(String username);

    List<GiteeRepository> searchRepositories(String username, int page, int perPage, String order);
}
