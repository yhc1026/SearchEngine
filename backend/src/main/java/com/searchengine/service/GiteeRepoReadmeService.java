package com.searchengine.service;

import com.searchengine.api.GiteeApiProperties;
import com.searchengine.dto.gitee.GiteeReadme;
import com.searchengine.service.impl.GiteeRepoReadmeServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.client.HttpClientErrorException;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

/**
 * Gitee 仓库 README Service（Service 后缀），实现 {@link GiteeRepoReadmeServiceImpl}。
 * 调用 GET /api/v5/repos/{owner}/{repo}/readme
 *
 * @see <a href="https://gitee.com/api/v5/swagger">Gitee API V5 - 获取仓库 README</a>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GiteeRepoReadmeService implements GiteeRepoReadmeServiceImpl {

    private static final String REPO_README_PATH = "/repos/{owner}/{repo}/readme";

    private final RestTemplate restTemplate;
    private final GiteeApiProperties giteeApiProperties;

    @Override
    public GiteeReadme getReadme(String ownerName, String repoName) {
        return getReadmeInternal(ownerName, repoName);
    }

    /**
     * 若 content 为 base64 编码，则解码为 UTF-8 字符串并写回 content。
     */
    private void decodeContentIfBase64(GiteeReadme readme) {
        if (readme == null || readme.getContent() == null || readme.getContent().isEmpty()) {
            return;
        }
        if (!"base64".equalsIgnoreCase(readme.getEncoding())) {
            return;
        }
        try {
            byte[] decoded = Base64.getDecoder().decode(readme.getContent());
            readme.setContent(new String(decoded, StandardCharsets.UTF_8));
        } catch (IllegalArgumentException e) {
            log.warn("README content Base64 解码失败，保留原内容: {}", e.getMessage());
        }
    }

    private GiteeReadme getReadmeInternal(String owner, String repo) {
        if (owner == null || owner.isBlank() || repo == null || repo.isBlank()) {
            return null;
        }
        String baseUrl = giteeApiProperties.getBaseUrl();
        if (baseUrl == null || baseUrl.isBlank()) {
            log.warn("Gitee baseUrl 未配置，请检查 gitee.api.base-url");
            return null;
        }

        URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path(REPO_README_PATH)
                .queryParamIfPresent("access_token", Optional.ofNullable(giteeApiProperties.getAccessToken()).filter(s -> !s.isBlank()))
                .buildAndExpand(owner.trim(), repo.trim())
                .encode()
                .toUri();

        log.info("Gitee 仓库 README 请求: owner={}, repo={}, uri={}", owner, repo, uri);

        try {
            ResponseEntity<GiteeReadme> response = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    null,
                    GiteeReadme.class
            );
            GiteeReadme body = response.getBody();
            if (body == null) {
                log.warn("Gitee 仓库 README 响应体为空: owner={}, repo={}", owner, repo);
                return null;
            }
            decodeContentIfBase64(body);
            return body;
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() == 404) {
                log.warn("Gitee 仓库或 README 不存在(404): owner={}, repo={}, uri={}", owner, repo, uri);
            } else {
                log.warn("Gitee 仓库 README HTTP 错误: owner={}, repo={}, status={}, body={}", owner, repo, e.getStatusCode(), e.getResponseBodyAsString());
            }
            return null;
        } catch (Exception e) {
            log.warn("Gitee 仓库 README 请求失败: owner={}, repo={}, uri={}, error={}", owner, repo, uri, e.getMessage(), e);
            return null;
        }
    }
}
