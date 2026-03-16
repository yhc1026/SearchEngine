package com.searchengine.service;

import com.searchengine.document.RepositoryDoc;
import org.springframework.data.domain.Page;

/**
 * 「仓库」在 Elasticsearch 里的索引与搜索服务接口。
 * <p>
 * 小白理解：这个接口定义了两件对外能做的事：
 * 1）把 MySQL 里仓库数据「同步到 ES」建索引（重建索引）；
 * 2）在 ES 里按关键词「搜索」仓库，并分页返回结果。
 * <p>
 * 实现类在 service.impl 包下，真正干活的是实现类；这里只规定「能做什么」。
 */
public interface RepositoryEsIndexService {

    /**
     * 全量重建 ES 索引：从 MySQL 的 repository 表把所有有 readme 的仓库读出来，
     * 转成 ES 文档后写入 ES。ES 会自动为这些文档建倒排索引，之后才能被搜索到。
     *
     * @return 本次写入 ES 的文档数量（条数）
     */
    long rebuildIndex();

    /**
     * 在 ES 里按关键词做全文检索（主要搜 readme 和 fullName），并分页返回。
     *
     * @param query 用户输入的关键词（会用来匹配 readme、fullName）
     * @param page  页码，从 0 开始（第 1 页就是 0）
     * @param size  每页条数
     * @return 一页结果，里面是 {@link RepositoryDoc} 列表 + 总条数等信息
     */
    Page<RepositoryDoc> search(String query, int page, int size);
}
