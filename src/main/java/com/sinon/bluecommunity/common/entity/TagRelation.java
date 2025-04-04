package com.sinon.bluecommunity.common.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 标签关系实体类
 */
@Data
public class TagRelation {
    /**
     * 关系ID
     */
    private Long id;

    /**
     * 标签ID
     */
    private Long tagId;

    /**
     * 目标ID（可以是帖子ID、活动ID等）
     */
    private Long targetId;

    /**
     * 目标类型（post、activity等）
     */
    private String targetType;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    // 目标类型常量
    public static final String TYPE_POST = "post";
    public static final String TYPE_ACTIVITY = "activity";
    public static final String TYPE_RESOURCE = "resource";

    /**
     * 验证目标类型是否有效
     */
    public boolean isValidTargetType() {
        return targetType != null && (
            TYPE_POST.equals(targetType) ||
            TYPE_ACTIVITY.equals(targetType) ||
            TYPE_RESOURCE.equals(targetType)
        );
    }

    /**
     * 验证关系是否有效
     */
    public boolean isValid() {
        return tagId != null && targetId != null && isValidTargetType();
    }

    /**
     * 创建帖子标签关系
     */
    public static TagRelation createPostRelation(Long tagId, Long postId) {
        TagRelation relation = new TagRelation();
        relation.setTagId(tagId);
        relation.setTargetId(postId);
        relation.setTargetType(TYPE_POST);
        return relation;
    }

    /**
     * 创建活动标签关系
     */
    public static TagRelation createActivityRelation(Long tagId, Long activityId) {
        TagRelation relation = new TagRelation();
        relation.setTagId(tagId);
        relation.setTargetId(activityId);
        relation.setTargetType(TYPE_ACTIVITY);
        return relation;
    }

    /**
     * 创建资源标签关系
     */
    public static TagRelation createResourceRelation(Long tagId, Long resourceId) {
        TagRelation relation = new TagRelation();
        relation.setTagId(tagId);
        relation.setTargetId(resourceId);
        relation.setTargetType(TYPE_RESOURCE);
        return relation;
    }
}
