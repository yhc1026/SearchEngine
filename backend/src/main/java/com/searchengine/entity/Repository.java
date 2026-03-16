package com.searchengine.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 仓库表实体，对应表：search_engine.repository
 */
@Data
@TableName("repository")
public class Repository implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键，自增，从 10000001 开始
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 仓库全名（如 owner/repo），对应列 fullName
     */
    private String fullName;

    /**
     * 仓库页面地址，对应列 htmlUrl
     */
    private String htmlUrl;

    /**
     * README 内容（长文本）
     */
    private String readme;

    /**
     * 仓库在 Gitee/GitHub 中的原始 ID（可选）
     */
    private Long repositoryId;
}

