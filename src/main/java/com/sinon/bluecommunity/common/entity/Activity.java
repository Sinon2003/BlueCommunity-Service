package com.sinon.bluecommunity.common.entity;

import lombok.Data;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 活动实体类
 */
@Data
public class Activity {
    /**
     * 活动ID
     */
    private Long id;

    /**
     * 创建者ID
     */
    private Long userId;

    /**
     * 创建者用户名
     */
    private String username;

    /**
     * 活动标题
     */
    private String title;

    /**
     * 活动类型
     */
    private String type;

    /**
     * 活动描述
     */
    private String description;

    /**
     * 封面图片URL
     */
    private String coverUrl;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    /**
     * 地点类型（线上/线下）
     */
    private String locationType;

    /**
     * 活动地点
     */
    private String location;

    /**
     * 最大参与人数
     */
    private Integer maxParticipants;

    /**
     * 当前参与人数
     */
    private Integer currentParticipants;

    /**
     * 活动须知
     */
    private String notice;

    /**
     * 状态(0:草稿,1:报名中,2:进行中,3:已结束,4:已取消)
     */
    private Integer status;

    /**
     * 浏览次数
     */
    private Integer views;

    /**
     * 点赞数
     */
    private Integer likes;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    // 状态常量
    public static final int STATUS_DRAFT = 0;
    public static final int STATUS_ENROLLING = 1;
    public static final int STATUS_IN_PROGRESS = 2;
    public static final int STATUS_FINISHED = 3;
    public static final int STATUS_CANCELLED = 4;

    // 活动类型常量
    public static final String TYPE_ONLINE = "online";
    public static final String TYPE_OFFLINE = "offline";
    public static final String TYPE_HYBRID = "hybrid";

    // 地点类型常量
    public static final String LOCATION_TYPE_ONLINE = "online";
    public static final String LOCATION_TYPE_OFFLINE = "offline";

    /**
     * 检查活动状态
     */
    public boolean isDraft() {
        return STATUS_DRAFT == this.status;
    }

    public boolean isEnrolling() {
        return STATUS_ENROLLING == this.status;
    }

    public boolean isInProgress() {
        return STATUS_IN_PROGRESS == this.status;
    }

    public boolean isFinished() {
        return STATUS_FINISHED == this.status;
    }

    public boolean isCancelled() {
        return STATUS_CANCELLED == this.status;
    }

    /**
     * 检查是否可以报名
     */
    public boolean canEnroll() {
        return isEnrolling() && 
               !isFullyBooked() && 
               LocalDateTime.now().isBefore(startTime);
    }

    /**
     * 检查是否已满
     */
    public boolean isFullyBooked() {
        return maxParticipants > 0 && currentParticipants >= maxParticipants;
    }

    /**
     * 增加参与人数
     */
    public boolean incrementParticipants() {
        if (isFullyBooked()) {
            return false;
        }
        this.currentParticipants = (this.currentParticipants == null ? 1 : this.currentParticipants + 1);
        return true;
    }

    /**
     * 减少参与人数
     */
    public void decrementParticipants() {
        this.currentParticipants = (this.currentParticipants == null || this.currentParticipants <= 0) 
            ? 0 : this.currentParticipants - 1;
    }

    /**
     * 初始化计数器
     */
    public void initializeCounters() {
        this.views = this.views == null ? 0 : this.views;
        this.likes = this.likes == null ? 0 : this.likes;
        this.currentParticipants = this.currentParticipants == null ? 0 : this.currentParticipants;
        this.maxParticipants = this.maxParticipants == null ? 0 : this.maxParticipants;
    }

    /**
     * 增加浏览次数
     */
    public void incrementViews() {
        this.views = (this.views == null ? 1 : this.views + 1);
    }

    /**
     * 增加点赞数
     */
    public void incrementLikes() {
        this.likes = (this.likes == null ? 1 : this.likes + 1);
    }

    /**
     * 减少点赞数
     */
    public void decrementLikes() {
        this.likes = (this.likes == null || this.likes <= 0) ? 0 : this.likes - 1;
    }

    /**
     * 验证活动类型是否有效
     */
    public boolean isValidType() {
        return type != null && (
            TYPE_ONLINE.equals(type) ||
            TYPE_OFFLINE.equals(type) ||
            TYPE_HYBRID.equals(type)
        );
    }

    /**
     * 验证地点类型是否有效
     */
    public boolean isValidLocationType() {
        return locationType != null && (
            LOCATION_TYPE_ONLINE.equals(locationType) ||
            LOCATION_TYPE_OFFLINE.equals(locationType)
        );
    }

    /**
     * 验证时间是否有效
     */
    public boolean isValidTime() {
        return startTime != null && endTime != null && 
               startTime.isBefore(endTime);
    }

    /**
     * 验证标题是否有效
     */
    public boolean isValidTitle() {
        return this.title != null && !this.title.trim().isEmpty() && 
               this.title.length() <= 100;
    }

    /**
     * 验证参与人数是否有效
     */
    public boolean isValidParticipants() {
        return maxParticipants == null || maxParticipants >= 0;
    }

    /**
     * 验证所有必填字段
     */
    public boolean isValid() {
        return isValidTitle() && isValidType() && isValidLocationType() && 
               isValidTime() && isValidParticipants();
    }

    /**
     * 更新活动状态（根据时间自动更新）
     */
    public void updateStatus() {
        if (this.status == STATUS_CANCELLED) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(startTime)) {
            this.status = STATUS_ENROLLING;
        } else if (now.isAfter(endTime)) {
            this.status = STATUS_FINISHED;
        } else {
            this.status = STATUS_IN_PROGRESS;
        }
    }
}
