package com.searchengine.dto.gitee;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Gitee 仓库（仅保留常用展示字段）
 *
 * @see <a href="https://gitee.com/api/v5/swagger">Gitee API V5 - 搜索仓库</a>
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GiteeRepository implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 仓库 ID */
    private Long id;
    /** 仓库名称 */
    private String name;
    /** 完整路径，如 owner/repo */
    @JsonProperty("full_name")
    private String fullName;
    /** 仓库路径（path） */
    private String path;
    /** 仓库主页 URL */
    @JsonProperty("html_url")
    private String htmlUrl;
    /** 仓库描述 */
    private String description;
    /** 主要语言 */
    private String language;
    /** Star 数 */
    @JsonProperty("stars_count")
    @JsonAlias("stargazers_count")
    private Integer starsCount;
    /** Fork 数 */
    @JsonProperty("forks_count")
    private Integer forksCount;
    /** 更新时间 */
    @JsonProperty("updated_at")
    private String updatedAt;
}
