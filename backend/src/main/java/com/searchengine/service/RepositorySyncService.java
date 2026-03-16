package com.searchengine.service;

import com.searchengine.entity.Repository;

import java.util.List;

/**
 * 仓库同步接口（不带 Impl 后缀）。
 */
public interface RepositorySyncService {

    List<Repository> syncByUsername(String username);
}


