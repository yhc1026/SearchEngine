package com.searchengine.service;

import com.searchengine.dto.gitee.GiteeUser;

import java.util.List;

/**
 * Gitee 用户搜索接口（不带 Impl 后缀）。
 */
public interface GiteeUserSearchService {

    List<GiteeUser> searchUsers(String q);

    List<GiteeUser> searchUsers(String q, int page, int perPage, String order);
}

