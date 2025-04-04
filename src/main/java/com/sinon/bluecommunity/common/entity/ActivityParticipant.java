package com.sinon.bluecommunity.common.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 活动参与者实体类
 */
@Data
public class ActivityParticipant {
    /**
     * ID
     */
    private Long id;

    /**
     * 活动ID
     */
    private Long activityId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 状态(0:待确认,1:已确认,2:已取消,3:已拒绝)
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    // 状态常量
    public static final int STATUS_PENDING = 0;
    public static final int STATUS_CONFIRMED = 1;
    public static final int STATUS_CANCELLED = 2;
    public static final int STATUS_REJECTED = 3;

    /**
     * 检查参与状态
     */
    public boolean isPending() {
        return STATUS_PENDING == this.status;
    }

    public boolean isConfirmed() {
        return STATUS_CONFIRMED == this.status;
    }

    public boolean isCancelled() {
        return STATUS_CANCELLED == this.status;
    }

    public boolean isRejected() {
        return STATUS_REJECTED == this.status;
    }

    /**
     * 确认参与
     */
    public void confirm() {
        this.status = STATUS_CONFIRMED;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 取消参与
     */
    public void cancel() {
        this.status = STATUS_CANCELLED;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 拒绝参与
     */
    public void reject() {
        this.status = STATUS_REJECTED;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 初始化默认值
     */
    public void initializeDefaults() {
        if (this.status == null) {
            this.status = STATUS_PENDING;
        }
    }

    /**
     * 验证是否有效
     */
    public boolean isValid() {
        return activityId != null && userId != null;
    }

    /**
     * 创建新的参与记录
     */
    public static ActivityParticipant create(Long activityId, Long userId) {
        ActivityParticipant participant = new ActivityParticipant();
        participant.setActivityId(activityId);
        participant.setUserId(userId);
        participant.setStatus(STATUS_PENDING);
        return participant;
    }
}
