package com.sinon.bluecommunity.user.mapper;

import com.sinon.bluecommunity.common.entity.ResourceReview;
import org.apache.ibatis.annotations.*;

/**
 * 资源审核记录数据访问层接口
 */
@Mapper
public interface ResourceReviewMapper {
    
    /**
     * 添加审核记录
     */
    @Insert("INSERT INTO resource_reviews (resource_id, reviewer_id, status, reason) VALUES (#{resourceId}, #{reviewerId}, #{status}, #{reason})")
    int insert(ResourceReview review);

    /**
     * 获取最新的审核记录
     */
    @Select("SELECT * FROM resource_reviews WHERE resource_id = #{resourceId} ORDER BY created_at DESC LIMIT 1")
    ResourceReview getLatestReview(Long resourceId);

    /**
     * 获取审核历史
     */
    @Select("SELECT * FROM resource_reviews WHERE resource_id = #{resourceId} ORDER BY created_at DESC")
    ResourceReview[] getReviewHistory(Long resourceId);
}
