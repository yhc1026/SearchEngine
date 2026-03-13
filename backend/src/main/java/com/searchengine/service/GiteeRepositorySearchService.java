package com.searchengine.service;

import com.searchengine.api.GiteeApiProperties;
import com.searchengine.dto.gitee.GiteeRepository;
import com.searchengine.service.impl.GiteeRepositorySearchServiceImpl;
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
 * Gitee 仓库搜索 Service（Service 后缀），实现 {@link GiteeRepositorySearchServiceImpl}。
 * 使用 Gitee 搜索仓库 API：GET /api/v5/search/repositories
 *
 * @see <a href="https://gitee.com/api/v5/swagger">Gitee API V5 - 搜索仓库</a>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GiteeRepositorySearchService implements GiteeRepositorySearchServiceImpl {

    /**
     * 搜索仓库 API（关键词搜索，匹配仓库名、描述等）
     * @see <a href="https://gitee.com/api/v5/search/repositories">GET /search/repositories</a>
     */
    private static final String SEARCH_REPOS_PATH = "/search/repositories";

    private final RestTemplate restTemplate;
    private final GiteeApiProperties giteeApiProperties;

    @Override
    public List<GiteeRepository> searchRepositories(String username) {
        return searchRepositories(username, 1, 20, "desc");
    }

    @Override
    public List<GiteeRepository> searchRepositories(String username, int page, int perPage, String order) {
        if (username == null || username.isBlank()) {
            return Collections.emptyList();
        }

        String baseUrl = giteeApiProperties.getBaseUrl();
        if (baseUrl == null || baseUrl.isBlank()) {
            log.warn("Gitee baseUrl 未配置，请检查 gitee.api.base-url");
            return Collections.emptyList();
        }

        URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl + SEARCH_REPOS_PATH)
                .queryParam("q", username.trim())
                .queryParam("page", page)
                .queryParam("per_page", perPage)
                .queryParam("order", order)
                .queryParamIfPresent("access_token", Optional.ofNullable(giteeApiProperties.getAccessToken()).filter(s -> !s.isBlank()))
                .build()
                .encode()
                .toUri();

        log.debug("Gitee 搜索仓库: {}", uri);

        try {
            ResponseEntity<List<GiteeRepository>> response = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
            );
            List<GiteeRepository> body = response.getBody();
            int size = body != null ? body.size() : 0;
            log.info("Gitee 搜索仓库: q={}, count={}", username, size);
            return body != null ? body : Collections.emptyList();
        } catch (Exception e) {
            log.warn("Gitee 搜索仓库失败: q={}, uri={}, error={}", username, uri, e.getMessage(), e);
            return Collections.emptyList();
        }
    }
}
