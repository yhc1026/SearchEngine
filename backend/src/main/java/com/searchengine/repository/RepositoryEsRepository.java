package com.searchengine.repository;

import com.searchengine.document.RepositoryDoc;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * 面向 Elasticsearch 的「仓库文档」数据访问层。
 * <p>
 * 小白理解：
 * - 我们存在 MySQL 里的叫「实体 Entity」，存在 ES 里的叫「文档 Document」。
 * - 这个接口专门负责：把「仓库文档」增删改查到 Elasticsearch，不碰 MySQL。
 * - 继承 {@link ElasticsearchRepository} 后，不用写实现类，Spring 会自动提供
 *   save、saveAll、findById、deleteById、count 等常用方法，直接注入就能用。
 * <p>
 * 泛型含义：RepositoryEsRepository 操作的是「RepositoryDoc」这种文档，主键类型是 Long（对应仓库 id）。
 */
@Repository
public interface RepositoryEsRepository extends ElasticsearchRepository<RepositoryDoc, Long> {
    // 不需要写任何方法，父类已经提供了 save、saveAll、findById、delete 等；
    // 若以后需要自定义查询（如按 fullName 查），可以在这里声明方法，Spring Data 会按命名规则自动实现。
}
