package com.searchengine.service.impl;

import com.searchengine.dto.gitee.GiteeReadme;

/**
 * Gitee 仓库 README 接口（Impl 后缀，位于 impl 目录）。
 * 实现类：{@link com.searchengine.service.GiteeRepoReadmeService}
 *
 * @see <a href="https://gitee.com/api/v5/swagger">GET /repos/{owner}/{repo}/readme</a>
 */
public interface GiteeRepoReadmeServiceImpl {

    /**
     * 获取仓库 README。
     *
     * @param ownerName 仓库所有者/组织 name（path）
     * @param repoName  仓库名字（path）
     * @return README 对象，失败或无 README 时返回 null
     */
    GiteeReadme getReadme(String ownerName, String repoName);
}
