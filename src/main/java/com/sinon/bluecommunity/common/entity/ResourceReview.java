package com.sinon.bluecommunity.common.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 资源审核记录
 */
@Data
public class ResourceReview {
    /**
     * 记录ID
     */
    private Long id;

    /**
     * 资源ID
     */
    private Long resourceId;

    /**
     * 审核人ID
     */
    private Long reviewerId;

    /**
     * 审核状态(0:待审核,1:通过,2:拒绝)
     */
    private Integer status;

    /**
     * 审核意见
     */
    private String reason;

    /**
     * 审核时间
     */
    private LocalDateTime createdAt;

    // 状态常量
    public static final int STATUS_PENDING = 0;
    public static final int STATUS_APPROVED = 1;
    public static final int STATUS_REJECTED = 2;
}
