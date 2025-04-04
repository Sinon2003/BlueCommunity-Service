package com.sinon.bluecommunity.common.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 点赞实体类
 */
@Data
public class Like {
    /**
     * 点赞ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 目标ID（帖子ID、活动ID等）
     */
    private Long targetId;

    /**
     * 目标类型
     */
    private String targetType;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    // 目标类型常量
    public static final String TARGET_TYPE_TOPIC = "topic";
    public static final String TARGET_TYPE_ACTIVITY = "activity";
    public static final String TARGET_TYPE_RESOURCE = "resource";
    public static final String TARGET_TYPE_COMMENT = "comment";

    /**
     * 验证目标类型是否有效
     */
    public boolean isValidTargetType() {
        return targetType != null && (
            TARGET_TYPE_TOPIC.equals(targetType) ||
            TARGET_TYPE_ACTIVITY.equals(targetType) ||
            TARGET_TYPE_RESOURCE.equals(targetType) ||
            TARGET_TYPE_COMMENT.equals(targetType)
        );
    }

    /**
     * 验证所有必填字段
     */
    public boolean isValid() {
        return userId != null && targetId != null && isValidTargetType();
    }

    /**
     * 创建帖子点赞
     */
    public static Like createTopicLike(Long topicId, Long userId) {
        Like like = new Like();
        like.setTargetId(topicId);
        like.setTargetType(TARGET_TYPE_TOPIC);
        like.setUserId(userId);
        return like;
    }

    /**
     * 创建活动点赞
     */
    public static Like createActivityLike(Long activityId, Long userId) {
        Like like = new Like();
        like.setTargetId(activityId);
        like.setTargetType(TARGET_TYPE_ACTIVITY);
        like.setUserId(userId);
        return like;
    }

    /**
     * 创建资源点赞
     */
    public static Like createResourceLike(Long resourceId, Long userId) {
        Like like = new Like();
        like.setTargetId(resourceId);
        like.setTargetType(TARGET_TYPE_RESOURCE);
        like.setUserId(userId);
        return like;
    }

    /**
     * 创建评论点赞
     */
    public static Like createCommentLike(Long commentId, Long userId) {
        Like like = new Like();
        like.setTargetId(commentId);
        like.setTargetType(TARGET_TYPE_COMMENT);
        like.setUserId(userId);
        return like;
    }
}
