package com.searchengine.service.impl;

import com.searchengine.api.GiteeApiProperties;
import com.searchengine.dto.gitee.GiteeRepository;
import com.searchengine.service.GiteeOrgReposService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Gitee 组织仓库 Service 实现类（Impl 后缀，位于 impl 目录）。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GiteeOrgReposServiceImpl implements GiteeOrgReposService {

    private static final String ORG_REPOS_PATH = "/orgs/{org}/repos";

    private final RestTemplate restTemplate;
    private final GiteeApiProperties giteeApiProperties;

    @Override
    public List<GiteeRepository> getOrgRepos(String org) {
        return getOrgRepos(org, 1, 20, "desc");
    }

    @Override
    public List<GiteeRepository> getOrgRepos(String org, int page, int perPage, String order) {
        if (org == null || org.isBlank()) {
            return Collections.emptyList();
        }

        String baseUrl = giteeApiProperties.getBaseUrl();
        if (baseUrl == null || baseUrl.isBlank()) {
            log.warn("Gitee baseUrl 未配置，请检查 gitee.api.base-url");
            return Collections.emptyList();
        }

        URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path(ORG_REPOS_PATH)
                .queryParam("page", page)
                .queryParam("per_page", perPage)
                .queryParam("sort", "updated")
                .queryParam("direction", order)
                .queryParamIfPresent("access_token", Optional.ofNullable(giteeApiProperties.getAccessToken()).filter(s -> !s.isBlank()))
                .buildAndExpand(org.trim())
                .encode()
                .toUri();

        log.debug("Gitee 组织仓库: {}", uri);

        try {
            ResponseEntity<List<GiteeRepository>> response = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
            );
            List<GiteeRepository> body = response.getBody();
            int size = body != null ? body.size() : 0;
            log.info("Gitee 组织仓库: org={}, count={}", org, size);
            return body != null ? body : Collections.emptyList();
        } catch (Exception e) {
            log.warn("Gitee 组织仓库请求失败: org={}, uri={}, error={}", org, uri, e.getMessage(), e);
            return Collections.emptyList();
        }
    }
}

