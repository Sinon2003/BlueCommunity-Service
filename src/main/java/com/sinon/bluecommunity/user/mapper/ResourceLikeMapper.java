package com.sinon.bluecommunity.user.mapper;

import com.sinon.bluecommunity.common.entity.ResourceLike;
import org.apache.ibatis.annotations.*;

/**
 * 资源点赞记录数据访问层接口
 */
@Mapper
public interface ResourceLikeMapper {
    
    /**
     * 添加点赞记录
     */
    @Insert("INSERT INTO resource_likes (resource_id, user_id) VALUES (#{resourceId}, #{userId})")
    int insert(ResourceLike like);

    /**
     * 删除点赞记录
     */
    @Delete("DELETE FROM resource_likes WHERE resource_id = #{resourceId} AND user_id = #{userId}")
    int delete(@Param("resourceId") Long resourceId, @Param("userId") Long userId);

    /**
     * 检查是否已点赞
     */
    @Select("SELECT COUNT(*) FROM resource_likes WHERE resource_id = #{resourceId} AND user_id = #{userId}")
    boolean exists(@Param("resourceId") Long resourceId, @Param("userId") Long userId);

    /**
     * 统计点赞数
     */
    @Select("SELECT COUNT(*) FROM resource_likes WHERE resource_id = #{resourceId}")
    int countByResourceId(Long resourceId);
}
