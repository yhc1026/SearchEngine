package com.searchengine.dto.gitee;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Gitee 仓库 README 接口返回对象。
 *
 * @see <a href="https://gitee.com/api/v5/swagger">GET /repos/{owner}/{repo}/readme</a>
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GiteeReadme implements Serializable {

    private static final long serialVersionUID = 1L;

    private String type;
    private String encoding;
    private Integer size;
    private String name;
    private String path;
    /** README 正文（接口返回时若为 base64 会解码为 UTF-8 字符串） */
    private String content;
    private String sha;
    private String url;
    @JsonProperty("html_url")
    private String htmlUrl;
    @JsonProperty("download_url")
    private String downloadUrl;
}
