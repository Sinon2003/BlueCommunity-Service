package com.sinon.bluecommunity.user.service;

import com.sinon.bluecommunity.common.entity.Like;
import java.util.List;

/**
 * 点赞服务接口
 */
public interface LikeService {
    
    /**
     * 点赞
     *
     * @param userId 用户ID
     * @param targetId 目标ID
     * @param targetType 目标类型
     * @throws IllegalArgumentException 如果目标类型无效
     */
    void like(Long userId, Long targetId, String targetType);
    
    /**
     * 取消点赞
     *
     * @param userId 用户ID
     * @param targetId 目标ID
     * @param targetType 目标类型
     */
    void unlike(Long userId, Long targetId, String targetType);
    
    /**
     * 检查是否已点赞
     *
     * @param userId 用户ID
     * @param targetId 目标ID
     * @param targetType 目标类型
     * @return true: 已点赞 false: 未点赞
     */
    boolean hasLiked(Long userId, Long targetId, String targetType);
    
    /**
     * 获取目标的点赞数
     *
     * @param targetId 目标ID
     * @param targetType 目标类型
     * @return 点赞数
     */
    int getLikeCount(Long targetId, String targetType);
    
    /**
     * 获取用户的点赞列表
     *
     * @param userId 用户ID
     * @param targetType 目标类型（可选）
     * @param page 页码
     * @param size 每页大小
     * @return 点赞列表
     */
    List<Like> getUserLikes(Long userId, String targetType, Integer page, Integer size);
    
    /**
     * 批量删除指定目标的点赞记录
     *
     * @param targetId 目标ID
     * @param targetType 目标类型
     * @return 删除的记录数
     */
    int deleteTargetLikes(Long targetId, String targetType);
    
    /**
     * 批量删除指定用户的点赞记录
     *
     * @param userId 用户ID
     * @return 删除的记录数
     */
    int deleteUserLikes(Long userId);
}
