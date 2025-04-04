package com.sinon.bluecommunity.user.mapper;

import com.sinon.bluecommunity.common.entity.TagRelation;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 标签关联数据访问层接口
 */
@Mapper
public interface TagRelationMapper {
    /**
     * 插入标签关联
     */
    @Insert("INSERT INTO tag_relations (tag_id, target_id, target_type) VALUES (#{tagId}, #{targetId}, #{targetType})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(TagRelation relation);

    /**
     * 插入标签关联（简化版）
     */
    @Insert("INSERT INTO tag_relations (tag_id, target_id, target_type) VALUES (#{tagId}, #{targetId}, #{targetType})")
    int insertSimple(@Param("tagId") Long tagId, @Param("targetId") Long targetId, @Param("targetType") String targetType);

    /**
     * 删除对象的所有标签关系
     */
    @Delete("DELETE FROM tag_relations WHERE target_id = #{targetId} AND target_type = #{targetType}")
    int deleteByTarget(@Param("targetId") Long targetId, @Param("targetType") String targetType);

    /**
     * 删除特定标签关系
     */
    @Delete("DELETE FROM tag_relations WHERE tag_id = #{tagId} AND target_id = #{targetId} AND target_type = #{targetType}")
    int deleteRelation(@Param("tagId") Long tagId, @Param("targetId") Long targetId, @Param("targetType") String targetType);

    /**
     * 检查标签关系是否存在
     */
    @Select("SELECT COUNT(*) FROM tag_relations WHERE tag_id = #{tagId} AND target_id = #{targetId} AND target_type = #{targetType}")
    boolean exists(@Param("tagId") Long tagId, @Param("targetId") Long targetId, @Param("targetType") String targetType);

    /**
     * 获取对象的标签关系列表
     */
    @Select("SELECT * FROM tag_relations WHERE target_id = #{targetId} AND target_type = #{targetType}")
    List<TagRelation> listByTarget(@Param("targetId") Long targetId, @Param("targetType") String targetType);

    /**
     * 获取标签的关联对象ID列表
     */
    @Select("SELECT target_id FROM tag_relations WHERE tag_id = #{tagId} AND target_type = #{targetType}")
    List<Long> listTargetIdsByTag(@Param("tagId") Long tagId, @Param("targetType") String targetType);

    /**
     * 统计标签的使用次数
     */
    @Select("SELECT COUNT(*) FROM tag_relations WHERE tag_id = #{tagId}")
    int countTagUsage(Long tagId);
}
