package com.searchengine.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.searchengine.entity.Repository;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 仓库表 Mapper，提供对 repository 表的 CRUD 操作。
 */
@Mapper
public interface RepositoryMapper extends BaseMapper<Repository> {

    /**
     * 插入单个仓库（等价于 BaseMapper#insert）
     */
    default int insertRepository(Repository repository) {
        return this.insert(repository);
    }

    /**
     * 批量插入仓库。
     * 说明：MyBatis-Plus 默认没有批量方法，这里仅定义接口，具体 SQL 可在 XML 中实现。
     */
    int batchInsert(@Param("list") List<Repository> list);

    /**
     * 根据主键 ID 查询仓库信息（等价于 BaseMapper#selectById）
     */
    default Repository selectByRepositoryId(Long id) {
        return this.selectById(id);
    }

    /**
     * 根据仓库名（fullName）查询仓库列表
     */
    List<Repository> selectByFullName(@Param("fullName") String fullName);

    /**
     * 根据主键 ID 更新仓库（等价于 BaseMapper#updateById）
     */
    default int updateByRepositoryId(Repository repository) {
        return this.updateById(repository);
    }

    /**
     * 根据主键 ID 删除仓库（等价于 BaseMapper#deleteById）
     */
    default int deleteByRepositoryId(Long id) {
        return this.deleteById(id);
    }
}

