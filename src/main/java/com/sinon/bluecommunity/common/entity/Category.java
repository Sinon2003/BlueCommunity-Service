package com.sinon.bluecommunity.common.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 分类实体类
 */
@Data
public class Category {
    /**
     * 分类ID
     */
    private Long id;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 分类描述
     */
    private String description;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 状态(0:禁用,1:正常)
     */
    private Integer status;

    /**
     * 排序顺序
     */
    private Integer sortOrder;

    /**
     * 图标URL
     */
    private String iconUrl;

    // 状态常量
    public static final int STATUS_DISABLED = 0;
    public static final int STATUS_NORMAL = 1;

    /**
     * 检查分类是否为正常状态
     */
    public boolean isNormal() {
        return STATUS_NORMAL == this.status;
    }

    /**
     * 检查分类是否被禁用
     */
    public boolean isDisabled() {
        return STATUS_DISABLED == this.status;
    }

    /**
     * 初始化默认值
     */
    public void initializeDefaults() {
        this.status = this.status == null ? STATUS_NORMAL : this.status;
        this.sortOrder = this.sortOrder == null ? 0 : this.sortOrder;
    }

    /**
     * 验证分类名称是否有效
     */
    public boolean isValidName() {
        return this.name != null && !this.name.trim().isEmpty() && this.name.length() <= 50;
    }

    /**
     * 验证排序顺序是否有效
     */
    public boolean isValidSortOrder() {
        return this.sortOrder != null && this.sortOrder >= 0;
    }

    /**
     * 验证图标URL是否有效
     */
    public boolean isValidIconUrl() {
        return this.iconUrl == null || this.iconUrl.length() <= 255;
    }
}
