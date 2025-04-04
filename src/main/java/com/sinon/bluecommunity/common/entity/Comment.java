package com.sinon.bluecommunity.common.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 评论实体类
 */
@Data
public class Comment {
    /**
     * 评论ID
     */
    private Long id;

    /**
     * 目标ID（帖子ID、活动ID等）
     */
    private Long targetId;

    /**
     * 目标类型
     */
    private String targetType;

    /**
     * 父评论ID
     */
    private Long parentId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 被回复用户ID
     */
    private Long replyUserId;

    /**
     * 评论层级 1:一级评论 2:二级评论
     */
    private Integer level;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 评论者用户名
     */
    private String username;

    /**
     * 评论者昵称
     */
    private String nickname;

    /**
     * 评论者头像
     */
    private String avatarUrl;

    /**
     * 被回复用户名
     */
    private String replyUsername;

    /**
     * 被回复用户昵称
     */
    private String replyNickname;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    // 目标类型常量
    public static final String TARGET_TYPE_TOPIC = "topic";
    public static final String TARGET_TYPE_ACTIVITY = "activity";
    public static final String TARGET_TYPE_RESOURCE = "resource";

    /**
     * 验证目标类型是否有效
     */
    public boolean isValidTargetType() {
        return targetType != null && (
            TARGET_TYPE_TOPIC.equals(targetType) ||
            TARGET_TYPE_ACTIVITY.equals(targetType) ||
            TARGET_TYPE_RESOURCE.equals(targetType)
        );
    }

    /**
     * 验证评论内容是否有效
     */
    public boolean isValidContent() {
        return content != null && !content.trim().isEmpty();
    }

    /**
     * 验证所有必填字段
     */
    public boolean isValid() {
        return targetId != null && userId != null && 
               isValidTargetType() && isValidContent();
    }

    /**
     * 创建帖子评论
     */
    public static Comment createTopicComment(Long topicId, Long userId, String content) {
        Comment comment = new Comment();
        comment.setTargetId(topicId);
        comment.setTargetType(TARGET_TYPE_TOPIC);
        comment.setUserId(userId);
        comment.setContent(content);
        return comment;
    }

    /**
     * 创建活动评论
     */
    public static Comment createActivityComment(Long activityId, Long userId, String content) {
        Comment comment = new Comment();
        comment.setTargetId(activityId);
        comment.setTargetType(TARGET_TYPE_ACTIVITY);
        comment.setUserId(userId);
        comment.setContent(content);
        return comment;
    }

    /**
     * 创建资源评论
     */
    public static Comment createResourceComment(Long resourceId, Long userId, String content) {
        Comment comment = new Comment();
        comment.setTargetId(resourceId);
        comment.setTargetType(TARGET_TYPE_RESOURCE);
        comment.setUserId(userId);
        comment.setContent(content);
        return comment;
    }
}