package com.sinon.bluecommunity.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 点赞数据访问接口
 */
@Mapper
public interface LikeMapper {
    
    /**
     * 添加点赞记录
     *
     * @param userId 用户ID
     * @param targetId 目标ID
     * @param targetType 目标类型(topic/activity/resource/comment)
     * @return 影响行数
     */
    int insert(@Param("userId") Long userId,
              @Param("targetId") Long targetId,
              @Param("targetType") String targetType);
    
    /**
     * 删除点赞记录
     *
     * @param userId 用户ID
     * @param targetId 目标ID
     * @param targetType 目标类型
     * @return 影响行数
     */
    int delete(@Param("userId") Long userId,
              @Param("targetId") Long targetId,
              @Param("targetType") String targetType);
    
    /**
     * 检查点赞记录是否存在
     *
     * @param userId 用户ID
     * @param targetId 目标ID
     * @param targetType 目标类型
     * @return true:存在 false:不存在
     */
    boolean exists(@Param("userId") Long userId,
                  @Param("targetId") Long targetId,
                  @Param("targetType") String targetType);
    
    /**
     * 获取目标的点赞数
     *
     * @param targetId 目标ID
     * @param targetType 目标类型
     * @return 点赞数
     */
    int countByTarget(@Param("targetId") Long targetId,
                     @Param("targetType") String targetType);
    
    /**
     * 获取用户的点赞数
     *
     * @param userId 用户ID
     * @param targetType 目标类型（可选）
     * @return 点赞数
     */
    int countByUser(@Param("userId") Long userId,
                   @Param("targetType") String targetType);
    
    /**
     * 统计用户点赞数
     */
    @Select("SELECT COUNT(*) FROM likes WHERE user_id = #{userId}")
    Integer countByUserId(@Param("userId") Long userId);
    
    /**
     * 批量删除指定目标的点赞记录
     *
     * @param targetId 目标ID
     * @param targetType 目标类型
     * @return 影响行数
     */
    int deleteByTarget(@Param("targetId") Long targetId,
                      @Param("targetType") String targetType);
    
    /**
     * 批量删除指定用户的点赞记录
     *
     * @param userId 用户ID
     * @return 影响行数
     */
    int deleteByUser(@Param("userId") Long userId);
}
