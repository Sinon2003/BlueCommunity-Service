package com.sinon.bluecommunity.common.vo;

import com.sinon.bluecommunity.common.entity.Comment;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 评论信息展示对象（包含回复）
 */
@Data
public class CommentVO {
    /**
     * 评论ID
     */
    private Long id;

    /**
     * 目标ID
     */
    private Long targetId;

    /**
     * 目标类型
     */
    private String targetType;

    /**
     * 评论用户ID
     */
    private Long userId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 评论者用户名
     */
    private String username;

    /**
     * 评论者昵称
     */
    private String nickname;

    /**
     * 评论者头像
     */
    private String avatarUrl;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 二级回复列表
     */
    private List<Comment> replies;

    /**
     * 从 Comment 实体转换为 VO
     */
    public static CommentVO fromComment(Comment comment) {
        if (comment == null) {
            return null;
        }

        CommentVO vo = new CommentVO();
        vo.setId(comment.getId());
        vo.setTargetId(comment.getTargetId());
        vo.setTargetType(comment.getTargetType());
        vo.setUserId(comment.getUserId());
        vo.setContent(comment.getContent());
        vo.setUsername(comment.getUsername());
        vo.setNickname(comment.getNickname());
        vo.setAvatarUrl(comment.getAvatarUrl());
        vo.setCreatedAt(comment.getCreatedAt());
        vo.setUpdatedAt(comment.getUpdatedAt());
        return vo;
    }
}
