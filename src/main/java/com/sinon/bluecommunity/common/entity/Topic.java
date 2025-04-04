package com.sinon.bluecommunity.common.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.data.annotation.Transient;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 帖子实体类
 */
@Data
public class Topic {
    /**
     * 帖子ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 帖子标题
     */
    @NotBlank(message = "标题不能为空")
    @Size(max = 100, message = "标题长度不能超过100个字符")
    @Size(min = 5, message = "标题长度不能少于5个字符")
    private String title;

    /**
     * 帖子内容
     */
    @NotBlank(message = "内容不能为空")
    private String content;

    /**
     * 封面图片URL
     */
    @Size(max = 255, message = "封面图片URL长度不能超过255个字符")
    private String coverUrl;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 浏览量
     */
    private Integer views;

    /**
     * 点赞数
     */
    private Integer likes;

    /**
     * 评论数
     */
    private Integer comments;

    /**
     * 是否置顶
     */
    private Boolean isPinned;

    /**
     * 状态(0:草稿,1:正常,2:已删除)
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
     * 标签列表（非数据库字段）
     */
    @Transient
    private List<String> tags;

    // 数据库状态常量
    public static final int STATUS_DRAFT = 0;
    public static final int STATUS_NORMAL = 1;
    public static final int STATUS_DELETED = 2;

    /**
     * 检查帖子是否为正常状态
     */
    public boolean isNormal() {
        return STATUS_NORMAL == this.status;
    }

    /**
     * 检查帖子是否已删除
     */
    public boolean isDeleted() {
        return STATUS_DELETED == this.status;
    }

    /**
     * 检查帖子是否为草稿
     */
    public boolean isDraft() {
        return STATUS_DRAFT == this.status;
    }

    /**
     * 增加浏览量
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
     * 增加评论数
     */
    public void incrementComments() {
        this.comments = (this.comments == null ? 1 : this.comments + 1);
    }

    /**
     * 减少评论数
     */
    public void decrementComments() {
        this.comments = (this.comments == null || this.comments <= 0) ? 0 : this.comments - 1;
    }

    /**
     * 初始化计数器
     */
    public void initializeCounters() {
        this.views = this.views == null ? 0 : this.views;
        this.likes = this.likes == null ? 0 : this.likes;
        this.comments = this.comments == null ? 0 : this.comments;
        this.isPinned = this.isPinned == null ? false : this.isPinned;
        this.status = this.status == null ? STATUS_DRAFT : this.status;
    }

    /**
     * 验证标题是否有效
     */
    public boolean isValidTitle() {
        return title != null && !title.trim().isEmpty() && title.length() <= 100;
    }

    /**
     * 验证内容是否有效
     */
    public boolean isValidContent() {
        return content != null && !content.trim().isEmpty();
    }

    /**
     * 验证封面URL是否有效
     */
    public boolean isValidCoverUrl() {
        return coverUrl == null || coverUrl.length() <= 255;
    }

    /**
     * 验证所有必填字段
     */
    public boolean isValid() {
        return userId != null && isValidTitle() && isValidContent() && isValidCoverUrl();
    }

    /**
     * 创建帖子
     */
    public static Topic create(Long userId, String title, String content) {
        Topic topic = new Topic();
        topic.setUserId(userId);
        topic.setTitle(title);
        topic.setContent(content);
        topic.initializeCounters();
        return topic;
    }

    /**
     * 创建草稿
     */
    public static Topic createDraft(Long userId, String title, String content) {
        Topic topic = create(userId, title, content);
        topic.setStatus(STATUS_DRAFT);
        return topic;
    }
}