package com.searchengine.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * Elasticsearch 索引文档：用于全文检索（倒排索引由 ES 自动维护）。
 */
@Data
@Document(indexName = "repositories")
public class RepositoryDoc {

    @Id
    private Long id;

    @Field(type = FieldType.Keyword)
    private String fullName;

    @Field(type = FieldType.Keyword)
    private String htmlUrl;

    @Field(type = FieldType.Text)
    private String readme;
}

