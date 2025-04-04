package com.sinon.bluecommunity.user.service;

import com.sinon.bluecommunity.common.entity.Follow;
import com.sinon.bluecommunity.common.vo.PageVO;

import java.util.List;

/**
 * 关注服务接口
 */
public interface FollowService {

    /**
     * 关注用户
     *
     * @param followerId 关注者ID
     * @param followeeId 被关注者ID
     * @throws IllegalArgumentException 如果关注者和被关注者相同
     * @throws IllegalStateException 如果已经关注
     */
    void follow(Long followerId, Long followeeId);

    /**
     * 取消关注
     *
     * @param followerId 关注者ID
     * @param followeeId 被关注者ID
     */
    void unfollow(Long followerId, Long followeeId);

    /**
     * 检查是否已关注
     *
     * @param followerId 关注者ID
     * @param followeeId 被关注者ID
     * @return true: 已关注 false: 未关注
     */
    boolean hasFollowed(Long followerId, Long followeeId);

    /**
     * 检查是否互相关注
     *
     * @param userId1 用户1 ID
     * @param userId2 用户2 ID
     * @return true: 互相关注 false: 未互相关注
     */
    boolean isMutualFollow(Long userId1, Long userId2);

    /**
     * 获取用户的关注列表
     *
     * @param followerId 关注者ID
     * @param page 页码（从1开始）
     * @param size 每页大小
     * @return 关注列表分页结果
     */
    PageVO<Follow> getFollowing(Long followerId, Integer page, Integer size);

    /**
     * 获取用户的粉丝列表
     *
     * @param followeeId 被关注者ID
     * @param page 页码（从1开始）
     * @param size 每页大小
     * @return 粉丝列表分页结果
     */
    PageVO<Follow> getFollowers(Long followeeId, Integer page, Integer size);

    /**
     * 获取用户的关注数
     *
     * @param followerId 关注者ID
     * @return 关注数
     */
    Long getFollowingCount(Long followerId);

    /**
     * 获取用户的粉丝数
     *
     * @param followeeId 被关注者ID
     * @return 粉丝数
     */
    Long getFollowersCount(Long followeeId);

    /**
     * 批量获取用户的关注状态
     *
     * @param followerId 关注者ID
     * @param followeeIds 被关注者ID列表
     * @return 关注列表，包含关注信息
     */
    List<Follow> getFollowStatus(Long followerId, List<Long> followeeIds);
}