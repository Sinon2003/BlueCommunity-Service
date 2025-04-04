package com.sinon.bluecommunity.user.service;

import com.sinon.bluecommunity.common.entity.Comment;
import com.sinon.bluecommunity.common.vo.CommentVO;

import java.util.List;

/**
 * 评论服务接口
 */
public interface CommentService {
    
    /**
     * 发布评论
     */
    Comment publishComment(Long targetId, String targetType, Long userId, String content);

    /**
     * 发布二级回复
     */
    Comment publishReply(Long targetId, String targetType, Long parentId, Long userId, Long replyUserId, String content);

    /**
     * 更新评论
     */
    Comment updateComment(Long commentId, Long userId, String content);

    /**
     * 删除评论
     */
    boolean deleteComment(Long commentId, Long userId);

    /**
     * 获取评论列表
     */
    List<Comment> getCommentList(Long targetId, String targetType, Long userId, String orderBy, Integer page, Integer size, Integer offset);

    /**
     * 获取二级回复列表
     */
    List<Comment> getReplyList(Long parentId, Integer page, Integer size, Integer offset);

    /**
     * 获取评论数量
     */
    Long getCommentCount(Long targetId, String targetType, Long userId);

    /**
     * 批量删除评论
     */
    int batchDeleteComments(List<Long> commentIds, Long userId);

    /**
     * 获取用户的评论列表
     */
    List<Comment> getUserComments(Long userId, Integer page, Integer size);

    /**
     * 获取最新评论
     */
    List<Comment> getLatestComments(Integer limit);

    /**
     * 检查是否有评论权限
     */
    boolean hasCommentPermission(Long commentId, Long userId);

    /**
     * 检查评论频率限制
     */
    boolean checkCommentFrequencyLimit(Long userId);

    /**
     * 清理用户的所有评论
     */
    int cleanUserComments(Long userId);

    /**
     * 获取评论列表（包含回复）
     */
    List<CommentVO> getCommentListWithReplies(Long targetId, String targetType, Long userId, String orderBy,
                                              Integer page, Integer size, Integer offset);
}
