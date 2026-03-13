package com.searchengine.service.impl;

import com.searchengine.dto.gitee.GiteeRepository;

import java.util.List;

/**
 * Gitee 组织仓库接口（Impl 后缀，位于 impl 目录）。
 * 实现类：{@link com.searchengine.service.GiteeOrgReposService}
 *
 * @see <a href="https://gitee.com/api/v5/swagger">GET /orgs/{org}/repos</a>
 */
public interface GiteeOrgReposServiceImpl {

    /**
     * 获取组织的仓库列表（默认第 1 页，每页 20 条）。
     *
     * @param org 组织 path（如 org 的登录名/path）
     * @return 仓库列表
     */
    List<GiteeRepository> getOrgRepos(String org);

    /**
     * 获取组织的仓库列表（分页与排序可指定）。
     *
     * @param org     组织 path
     * @param page    页码，从 1 开始
     * @param perPage 每页条数
     * @param order   排序方向：asc / desc
     * @return 仓库列表
     */
    List<GiteeRepository> getOrgRepos(String org, int page, int perPage, String order);
}
