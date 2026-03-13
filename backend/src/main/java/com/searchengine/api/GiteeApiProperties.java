package com.searchengine.api;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Gitee API 配置（baseUrl、accessToken 等），封装在 api 目录下。
 */
@Data
@Component
@ConfigurationProperties(prefix = "gitee.api")
public class GiteeApiProperties {

    /**
     * API 基础地址，默认 https://gitee.com/api/v5
     */
    private String baseUrl = "https://gitee.com/api/v5";

    /**
     * 个人 access_token，用于提高限流与权限（可选）
     */
    private String accessToken = "";
}
