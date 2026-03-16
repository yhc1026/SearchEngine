package com.searchengine.service;

import com.searchengine.dto.gitee.GiteeRepository;

import java.util.List;

/**
 * Gitee 组织仓库接口（不带 Impl 后缀）。
 */
public interface GiteeOrgReposService {

    List<GiteeRepository> getOrgRepos(String org);

    List<GiteeRepository> getOrgRepos(String org, int page, int perPage, String order);
}

