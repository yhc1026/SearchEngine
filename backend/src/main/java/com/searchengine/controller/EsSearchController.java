package com.searchengine.controller;

import com.searchengine.document.RepositoryDoc;
import com.searchengine.service.RepositoryEsIndexService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Elasticsearch 相关的 HTTP 接口：提供「重建索引」和「搜索」两个能力。
 * <p>
 * 小白理解：
 * - Controller 负责接收前端的 HTTP 请求，调用 Service 干活，再把结果返回给前端。
 * - 所有接口路径都以 /api/es 开头，例如：POST /api/es/index/rebuild、GET /api/es/search。
 */
@RestController
@RequestMapping("/api/es")
@RequiredArgsConstructor
public class EsSearchController {

    /** 索引与搜索的具体逻辑都在这个 Service 里，Controller 只做「转发」和「返回」。 */
    private final RepositoryEsIndexService repositoryEsIndexService;

    /**
     * 重建索引接口。无参
     * <p>
     * 调用方式：POST /api/es/index/rebuild（无需请求体）。
     * 作用：从 MySQL 的 repository 表把数据同步到 Elasticsearch，相当于「把仓库数据重新灌进 ES」。
     * 何时用：第一次使用 ES 搜索前、或者 MySQL 里仓库数据有大量更新后，调用一次即可。
     *
     * @return 例如 {"ok": true, "count": 10} 表示成功，并写入了 10 条文档
     */
    @PostMapping("/index/rebuild")
    public Map<String, Object> rebuild() {
        long count = repositoryEsIndexService.rebuildIndex();
        return Map.of("ok", true, "count", count);
    }

    /**
     * 搜索接口。
     * <p>
     * 调用方式：GET /api/es/search?q=关键词&page=0&size=20
     * 作用：在 ES 里用关键词 q 搜索仓库（主要搜 readme 和 fullName），分页返回。
     *
     * @param q    必填，个人或组织的用户名
     * @param page 可选，页码，默认 0（第一页）
     * @param size 可选，每页条数，默认 20
     * @return 一页搜索结果，类型是 Spring 的 Page，里面包含 content（文档列表）、总条数、总页数等
     */
    @GetMapping("/search")
    public Page<RepositoryDoc> search(
            @RequestParam("q") String q,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "20") int size) {
        return repositoryEsIndexService.search(q, page, size);
    }
}
