package com.searchengine.service.impl;

import com.searchengine.dto.gitee.GiteeUser;

import java.util.List;

/**
 * Gitee 用户搜索接口（Impl 后缀，位于 impl 目录）。
 * 实现类：{@link com.searchengine.service.GiteeUserSearchService}
 */
public interface GiteeUserSearchServiceImpl {

    List<GiteeUser> searchUsers(String q);

    List<GiteeUser> searchUsers(String q, int page, int perPage, String order);
}
