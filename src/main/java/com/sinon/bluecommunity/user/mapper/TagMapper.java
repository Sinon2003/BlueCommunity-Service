package com.sinon.bluecommunity.user.mapper;

import com.sinon.bluecommunity.common.entity.Tag;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 标签数据访问层接口
 */
@Mapper
public interface TagMapper {

    /**
     * 根据状态获取标签列表
     */
    @Select("SELECT * FROM tags WHERE status = #{status} ORDER BY usage_count DESC")
    List<Tag> listByStatus(Integer status);

    /**
     * 更新标签状态
     */
    @Update("UPDATE tags SET status = #{status} WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    /**
     * 检查标签名是否存在
     */
    @Select("SELECT COUNT(*) FROM tags WHERE name = #{name} AND id != #{excludeId}")
    int checkNameExists(@Param("name") String name, @Param("excludeId") Long excludeId);

    /**
     * 增加使用次数
     */
    @Update("UPDATE tags SET usage_count = usage_count + 1 WHERE id = #{id}")
    int incrementUsageCount(Long id);

    /**
     * 减少使用次数
     */
    @Update("UPDATE tags SET usage_count = usage_count - 1 WHERE id = #{id} AND usage_count > 0")
    int decrementUsageCount(Long id);

    /**
     * 根据名称模糊搜索标签
     */
    @Select("SELECT * FROM tags WHERE status = 1 AND name LIKE CONCAT('%', #{keyword}, '%') ORDER BY usage_count DESC LIMIT #{limit}")
    List<Tag> searchByName(@Param("keyword") String keyword, @Param("limit") Integer limit);

    /**
     * 获取热门标签
     */
    @Select("SELECT * FROM tags WHERE status = 1 ORDER BY usage_count DESC LIMIT #{limit}")
    List<Tag> getHotTags(Integer limit);

    /**
     * 根据对象ID和类型获取标签列表
     */
    @Select("SELECT t.* FROM tags t " +
            "INNER JOIN tag_relations tr ON t.id = tr.tag_id " +
            "WHERE tr.target_id = #{targetId} AND tr.target_type = #{targetType} AND t.status = 1")
    List<Tag> getTagsByTarget(@Param("targetId") Long targetId, @Param("targetType") String targetType);

    /**
     * 插入标签
     */
    @Insert("INSERT INTO tags (name, description, status, usage_count) VALUES (#{name}, #{description}, #{status}, #{usageCount})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Tag tag);

    /**
     * 根据名称获取标签ID
     */
    @Select("SELECT id FROM tags WHERE name = #{name}")
    Long getIdByName(String name);

    /**
     * 根据名称查询标签
     */
    @Select("SELECT * FROM tags WHERE name = #{name}")
    Tag selectByName(String name);

    /**
     * 根据话题ID查询标签列表
     */
    @Select("SELECT t.* FROM tags t " +
            "INNER JOIN tag_relations tr ON t.id = tr.tag_id " +
            "WHERE tr.target_id = #{topicId} AND tr.target_type = 'topic' " +
            "AND t.status = 1 " +
            "ORDER BY t.usage_count DESC")
    List<Tag> selectByTopicId(Long topicId);
}
