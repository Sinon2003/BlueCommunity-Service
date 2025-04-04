package com.sinon.bluecommunity.common.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 资源实体类
 */
@Data
public class Resource {
    /**
     * 资源ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 分享者用户名
     */
    private String username;

    /**
     * 资源标题
     */
    private String title;

    /**
     * 资源类型（如：文档、视频、音频等）
     */
    private String type;

    /**
     * URL类型（如：本地存储、外部链接等）
     */
    private String urlType;

    /**
     * 资源URL
     */
    private String resourceUrl;

    /**
     * 资源描述
     */
    private String description;

    /**
     * 资源内容
     */
    private String content;

    /**
     * 封面图片URL
     */
    private String coverUrl;

    /**
     * 下载次数
     */
    private Integer downloads;

    /**
     * 浏览次数
     */
    private Integer views;

    /**
     * 点赞数
     */
    private Integer likes;

    /**
     * 状态(0:待审核,1:正常,2:已删除,3:审核未通过)
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

    /**
     * 当前用户是否已点赞
     */
    private Boolean liked;

    /**
     * 当前用户是否已下载
     */
    private Boolean downloaded;

    // 状态常量
    public static final int STATUS_PENDING = 0;
    public static final int STATUS_NORMAL = 1;
    public static final int STATUS_DELETED = 2;
    public static final int STATUS_REJECTED = 3;

    // 资源类型常量
    public static final String TYPE_LEARNING = "learning";
    public static final String TYPE_PROJECT = "project";
    public static final String TYPE_VIDEO = "video";
    public static final String TYPE_DOCUMENT = "document";
    public static final String TYPE_TOOL = "tool";
    public static final String TYPE_OTHER = "other";

    // URL类型常量
    public static final String URL_TYPE_WEBSITE = "website";
    public static final String URL_TYPE_VIDEO = "video";
    public static final String URL_TYPE_DOCUMENT = "document";
    public static final String URL_TYPE_REPOSITORY = "repository";

    /**
     * 检查资源状态
     */
    public boolean isValid() {
        return status != null && status == STATUS_NORMAL;
    }

    public boolean isPending() {
        return status != null && status == STATUS_PENDING;
    }

    public boolean isDeleted() {
        return status != null && status == STATUS_DELETED;
    }

    public boolean isRejected() {
        return status != null && status == STATUS_REJECTED;
    }

    /**
     * 初始化计数器
     */
    public void initializeCounters() {
        this.downloads = this.downloads == null ? 0 : this.downloads;
        this.views = this.views == null ? 0 : this.views;
        this.likes = this.likes == null ? 0 : this.likes;
    }

    /**
     * 增加下载次数
     */
    public void incrementDownloads() {
        this.downloads = (this.downloads == null ? 1 : this.downloads + 1);
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
     * 验证资源类型是否有效
     */
    public boolean isValidType() {
        return type != null && (
            TYPE_LEARNING.equals(type) ||
            TYPE_PROJECT.equals(type) ||
            TYPE_VIDEO.equals(type) ||
            TYPE_DOCUMENT.equals(type) ||
            TYPE_TOOL.equals(type) ||
            TYPE_OTHER.equals(type)
        );
    }

    /**
     * 验证URL类型是否有效
     */
    public boolean isValidUrlType() {
        return urlType != null && (
            URL_TYPE_WEBSITE.equals(urlType) ||
            URL_TYPE_VIDEO.equals(urlType) ||
            URL_TYPE_DOCUMENT.equals(urlType) ||
            URL_TYPE_REPOSITORY.equals(urlType)
        );
    }

    /**
     * 验证标题是否有效
     */
    public boolean isValidTitle() {
        return this.title != null && !this.title.trim().isEmpty() && this.title.length() <= 100;
    }

    /**
     * 验证资源URL是否有效
     */
    public boolean isValidResourceUrl() {
        return this.resourceUrl != null && !this.resourceUrl.trim().isEmpty() && 
               this.resourceUrl.length() <= 255;
    }

    /**
     * 验证封面URL是否有效
     */
    public boolean isValidCoverUrl() {
        return this.coverUrl == null || this.coverUrl.length() <= 255;
    }

    /**
     * 验证所有必填字段
     */
    public boolean isValidAll() {
        return isValidTitle() && isValidType() && isValidUrlType() && 
               isValidResourceUrl() && isValidCoverUrl();
    }
}
