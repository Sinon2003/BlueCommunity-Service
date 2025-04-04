package com.sinon.bluecommunity.common.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 标签实体类
 */
@Data
public class Tag {
    /**
     * 标签ID
     */
    private Long id;

    /**
     * 标签名称
     */
    private String name;

    /**
     * 标签描述
     */
    private String description;

    /**
     * 使用次数
     */
    private Integer usageCount;

    /**
     * 状态(0:禁用,1:正常)
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
    public static final int STATUS_DISABLED = 0;
    public static final int STATUS_NORMAL = 1;

    /**
     * 检查标签是否为正常状态
     */
    public boolean isNormal() {
        return STATUS_NORMAL == this.status;
    }

    /**
     * 检查标签是否被禁用
     */
    public boolean isDisabled() {
        return STATUS_DISABLED == this.status;
    }

    /**
     * 初始化默认值
     */
    public void initializeDefaults() {
        this.status = this.status == null ? STATUS_NORMAL : this.status;
        this.usageCount = this.usageCount == null ? 0 : this.usageCount;
    }

    /**
     * 增加使用次数
     */
    public void incrementUsageCount() {
        this.usageCount = (this.usageCount == null ? 1 : this.usageCount + 1);
    }

    /**
     * 减少使用次数
     */
    public void decrementUsageCount() {
        this.usageCount = (this.usageCount == null || this.usageCount <= 0) ? 0 : this.usageCount - 1;
    }

    /**
     * 验证标签名称是否有效
     */
    public boolean isValidName() {
        return this.name != null && !this.name.trim().isEmpty() && this.name.length() <= 50;
    }

    /**
     * 验证标签描述是否有效
     */
    public boolean isValidDescription() {
        return this.description == null || this.description.length() <= 255;
    }
}
