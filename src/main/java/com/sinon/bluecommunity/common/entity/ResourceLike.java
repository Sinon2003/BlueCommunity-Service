package com.sinon.bluecommunity.common.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 资源点赞记录
 */
@Data
public class ResourceLike {
    /**
     * 记录ID
     */
    private Long id;

    /**
     * 资源ID
     */
    private Long resourceId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}
