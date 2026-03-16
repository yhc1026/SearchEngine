package com.searchengine.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.searchengine.document.RepositoryDoc;
import com.searchengine.entity.Repository;
import com.searchengine.mapper.RepositoryMapper;
import com.searchengine.repository.RepositoryEsRepository;
import com.searchengine.service.RepositoryEsIndexService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 「仓库」在 Elasticsearch 里的索引与搜索服务的具体实现。
 * <p>
 * 小白理解：
 * - 数据来源是 MySQL 的 repository 表（通过 RepositoryMapper 查）；
 * - 数据写入/检索的是 Elasticsearch（通过 RepositoryEsRepository 和 ElasticsearchOperations 操作）；
 * - 本类做两件事：① 从 MySQL 读数据 → 转成 ES 文档 → 写入 ES（重建索引）；
 *                ② 根据关键词在 ES 里搜索 → 组装成分页结果返回。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RepositoryEsIndexServiceImpl implements RepositoryEsIndexService {

    /** 查 MySQL 的 repository 表（我们真正的数据源）。 */
    private final RepositoryMapper repositoryMapper;
    /** 向 ES 写入/读取「仓库文档」，提供 saveAll、findById 等。 */
    private final RepositoryEsRepository repositoryEsRepository;
    /** 做更底层的 ES 操作，例如创建索引、执行复杂查询。 */
    private final ElasticsearchOperations elasticsearchOperations;

    /**
     * 全量重建 ES 索引：确保索引存在 → 从 MySQL 拉取有 readme 的仓库 → 转成文档 → 批量写入 ES。
     */
    @Override
    public long rebuildIndex() {
        // ---------- 1. 确保 ES 里「repositories」这个索引存在 ----------
        // IndexOperations：针对某个文档类型（RepositoryDoc）的索引的增删查等管理
        IndexOperations indexOps = elasticsearchOperations.indexOps(RepositoryDoc.class);
        if (!indexOps.exists()) {
            indexOps.create();                          // 创建索引
            indexOps.putMapping(indexOps.createMapping()); // 按 RepositoryDoc 的字段定义建 mapping（哪些字段要分词、哪些做 keyword 等）
        }

        // ---------- 2. 从 MySQL 查出所有「有 readme」的仓库 ----------
        LambdaQueryWrapper<Repository> wrapper = new LambdaQueryWrapper<Repository>()
                .select(Repository::getId, Repository::getFullName, Repository::getHtmlUrl, Repository::getReadme)
                .isNotNull(Repository::getReadme)
                .ne(Repository::getReadme, "");
        List<Repository> repos = repositoryMapper.selectList(wrapper);

        // ---------- 3. 把 MySQL 实体转成 ES 文档（RepositoryDoc） ----------
        List<RepositoryDoc> docs = new ArrayList<>(repos.size());
        for (Repository r : repos) {
            if (r == null || r.getId() == null) continue;
            RepositoryDoc doc = new RepositoryDoc();
            doc.setId(r.getId());
            doc.setFullName(r.getFullName());
            doc.setHtmlUrl(r.getHtmlUrl());
            doc.setReadme(r.getReadme());
            docs.add(doc);
        }

        // ---------- 4. 批量写入 ES（ES 会自动为 readme 等字段建倒排索引，之后就能被搜索） ----------
        repositoryEsRepository.saveAll(docs);
        log.info("ES index rebuilt: {} docs", docs.size());
        return docs.size();
    }

    /**
     * 在 ES 里按关键词搜索 readme 和 fullName，分页返回。
     */
    @Override
    public Page<RepositoryDoc> search(String query, int page, int size) {
        // 参数简单校验：避免负数页码、过大的 size
        String q = query == null ? "" : query.trim();
        int p = Math.max(page, 0);
        int s = Math.min(Math.max(size, 1), 100);
        PageRequest pageable = PageRequest.of(p, s);

        if (q.isEmpty()) {
            return Page.empty(pageable);
        }

        // 构造查询条件：readme 里匹配 q 或者 fullName 里匹配 q（满足其一即可）
        Criteria criteria = new Criteria("readme").matches(q)
                .or(new Criteria("fullName").matches(q));
        CriteriaQuery cq = new CriteriaQuery(criteria);
        cq.setPageable(pageable);

        // 执行搜索，得到一页命中的文档
        var hits = elasticsearchOperations.search(cq, RepositoryDoc.class);
        List<RepositoryDoc> content = hits.getSearchHits().stream()
                .map(h -> h.getContent())
                .filter(Objects::nonNull)
                .toList();

        // 封装成 Spring 的 Page 对象（包含当前页数据 + 总条数，方便前端分页）
        return new PageImpl<>(content, pageable, hits.getTotalHits());
    }
}
