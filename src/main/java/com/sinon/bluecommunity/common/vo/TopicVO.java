package com.sinon.bluecommunity.common.vo;

import com.sinon.bluecommunity.common.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 话题信息展示对象
 */
@Data
@NoArgsConstructor
public class TopicVO {
    
    /**
     * 话题ID
     */
    private Long id;
    
    /**
     * 作者ID
     */
    private Long userId;
    
    /**
     * 作者信息
     */
    private User author;
    
    /**
     * 标题
     */
    private String title;
    
    /**
     * 内容
     */
    private String content;
    
    /**
     * 封面图片URL
     */
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
     * 标签列表
     */
    private List<TagVO> tags;

    /**
     * 用户话题统计
     */
    @Data
    @NoArgsConstructor
    public static class UserTopicStats {
        /**
         * 总话题数
         */
        private Integer totalCount;
        
        /**
         * 已发布数量
         */
        private Integer publishedCount;
        
        /**
         * 草稿数量
         */
        private Integer draftCount;
    }
    
    /**
     * 分类话题统计
     */
    @Data
    @NoArgsConstructor
    public static class CategoryTopicStats {
        /**
         * 总话题数
         */
        private Integer totalCount;
        
        /**
         * 正常状态的话题数
         */
        private Integer normalCount;
    }
}
