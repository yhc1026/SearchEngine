package com.searchengine.service;

import com.searchengine.dto.gitee.GiteeReadme;

/**
 * Gitee 仓库 README 接口（不带 Impl 后缀）。
 */
public interface GiteeRepoReadmeService {

    GiteeReadme getReadme(String ownerName, String repoName);
}

