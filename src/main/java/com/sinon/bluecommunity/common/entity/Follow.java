package com.sinon.bluecommunity.common.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 关注关系实体类
 */
@Data
public class Follow {
    /**
     * 关注ID
     */
    private Long id;

    /**
     * 关注者ID
     */
    private Long followerId;

    /**
     * 被关注者ID
     */
    private Long followeeId;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 验证关注关系是否有效
     */
    public boolean isValid() {
        return followerId != null && followeeId != null && !followerId.equals(followeeId);
    }

    /**
     * 创建关注关系
     */
    public static Follow create(Long followerId, Long followeeId) {
        Follow follow = new Follow();
        follow.setFollowerId(followerId);
        follow.setFolloweeId(followeeId);
        return follow;
    }

    /**
     * 检查是否为互相关注
     * @param otherFollow 另一个关注关系
     * @return 是否互相关注
     */
    public boolean isMutualFollow(Follow otherFollow) {
        if (otherFollow == null) {
            return false;
        }
        return this.followerId.equals(otherFollow.getFolloweeId()) &&
               this.followeeId.equals(otherFollow.getFollowerId());
    }
}
