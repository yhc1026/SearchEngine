package com.searchengine.service;

import com.searchengine.dto.gitee.GiteeRepository;

import java.util.List;

/**
 * Gitee 仓库搜索接口（不带 Impl 后缀）。
 */
public interface GiteeRepositorySearchService {

    List<GiteeRepository> searchRepositories(String username);

    List<GiteeRepository> searchRepositories(String username, int page, int perPage, String order);
}

