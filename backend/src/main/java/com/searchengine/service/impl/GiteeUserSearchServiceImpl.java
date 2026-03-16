package com.searchengine.service.impl;

import com.searchengine.api.GiteeApiProperties;
import com.searchengine.dto.gitee.GiteeUser;
import com.searchengine.service.GiteeUserSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Gitee 用户搜索 Service 实现类（Impl 后缀，位于 impl 目录）。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GiteeUserSearchServiceImpl implements GiteeUserSearchService {

    private static final String SEARCH_USERS_PATH = "/search/users";

    private final RestTemplate restTemplate;
    private final GiteeApiProperties giteeApiProperties;

    @Override
    public List<GiteeUser> searchUsers(String q) {
        return searchUsers(q, 1, 20, "desc");
    }

    @Override
    public List<GiteeUser> searchUsers(String q, int page, int perPage, String order) {
        if (q == null || q.isBlank()) {
            return Collections.emptyList();
        }

        String url = UriComponentsBuilder.fromHttpUrl(giteeApiProperties.getBaseUrl() + SEARCH_USERS_PATH)
                .queryParam("q", q.trim())
                .queryParam("page", page)
                .queryParam("per_page", perPage)
                .queryParam("order", order)
                .queryParamIfPresent("access_token", Optional.ofNullable(giteeApiProperties.getAccessToken()).filter(s -> !s.isBlank()))
                .build()
                .toUriString();

        try {
            ResponseEntity<List<GiteeUser>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
            );
            List<GiteeUser> body = response.getBody();
            return body != null ? body : Collections.emptyList();
        } catch (Exception e) {
            log.warn("Gitee 用户搜索请求失败: q={}, error={}", q, e.getMessage());
            return Collections.emptyList();
        }
    }
}

