package com.searchengine.dto.gitee;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Gitee 用户（仅保留常用展示字段）
 */
@Data
public class GiteeUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 用户 ID */
    private Long id;
    /** 登录名 / 用户名 */
    private String login;
    /** 显示名称 */
    private String name;
    /** 头像 URL */
    @JsonProperty("avatar_url")
    private String avatarUrl;
    /** API 地址 */
    private String url;
    /** 用户主页 URL */
    @JsonProperty("html_url")
    private String htmlUrl;
}
