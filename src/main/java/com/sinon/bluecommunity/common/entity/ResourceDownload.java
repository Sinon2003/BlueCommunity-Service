package com.sinon.bluecommunity.common.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 资源下载记录
 */
@Data
public class ResourceDownload {
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
     * 下载时间
     */
    private LocalDateTime createdAt;
}
