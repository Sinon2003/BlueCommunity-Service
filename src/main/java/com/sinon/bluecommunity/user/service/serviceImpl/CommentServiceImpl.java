package com.sinon.bluecommunity.user.service.serviceImpl;

import com.sinon.bluecommunity.common.entity.Comment;
import com.sinon.bluecommunity.common.exception.BusinessException;
import com.sinon.bluecommunity.common.utils.RedisUtils;
import com.sinon.bluecommunity.common.vo.CommentVO;
import com.sinon.bluecommunity.user.mapper.CommentMapper;
import com.sinon.bluecommunity.user.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 评论服务实现类
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private RedisUtils redisUtils;

    private static final String COMMENT_FREQUENCY_KEY = "comment:frequency:";
    private static final int MAX_COMMENTS_PER_MINUTE = 5;
    private static final int CONTENT_MAX_LENGTH = 1000;
    
    @Override
    @Transactional
    public Comment publishComment(Long targetId, String targetType, Long userId, String content) {
        Assert.notNull(targetId, "目标ID不能为空");
        Assert.hasText(targetType, "目标类型不能为空");
        Assert.notNull(userId, "用户ID不能为空");
        Assert.hasText(content, "评论内容不能为空");

        // 检查评论频率
        if (!checkCommentFrequencyLimit(userId)) {
            throw new BusinessException("评论太频繁，请稍后再试");
        }

        // 检查内容合法性
        if (!isValidCommentContent(content)) {
            throw new BusinessException("评论内容包含非法内容");
        }

        Comment comment = new Comment();
        comment.setTargetId(targetId);
        comment.setTargetType(targetType);
        comment.setUserId(userId);
        comment.setContent(content);
        comment.setLevel(1); // 设置为一级评论

        commentMapper.insert(comment);
        return comment;
    }

    @Override
    @Transactional
    public Comment publishReply(Long targetId, String targetType, Long parentId, Long userId, Long replyUserId, String content) {
        Assert.notNull(targetId, "目标ID不能为空");
        Assert.hasText(targetType, "目标类型不能为空");
        Assert.notNull(parentId, "父评论ID不能为空");
        Assert.notNull(userId, "用户ID不能为空");
        Assert.notNull(replyUserId, "被回复用户ID不能为空");
        Assert.hasText(content, "评论内容不能为空");

        // 检查父评论是否存在
        Comment parentComment = commentMapper.getById(parentId);
        if (parentComment == null) {
            throw new BusinessException("父评论不存在");
        }

        // 检查父评论是否为一级评论
        if (parentComment.getLevel() != 1) {
            throw new BusinessException("只能回复一级评论");
        }

        // 检查评论频率
        if (!checkCommentFrequencyLimit(userId)) {
            throw new BusinessException("评论太频繁，请稍后再试");
        }

        // 检查内容合法性
        if (!isValidCommentContent(content)) {
            throw new BusinessException("评论内容包含非法内容");
        }

        Comment comment = new Comment();
        comment.setTargetId(targetId);
        comment.setTargetType(targetType);
        comment.setParentId(parentId);
        comment.setUserId(userId);
        comment.setReplyUserId(replyUserId);
        comment.setContent(content);
        comment.setLevel(2); // 设置为二级评论

        commentMapper.insert(comment);
        return comment;
    }

    @Override
    @Transactional
    public Comment updateComment(Long commentId, Long userId, String content) {
        Assert.notNull(commentId, "评论ID不能为空");
        Assert.notNull(userId, "用户ID不能为空");
        Assert.hasText(content, "评论内容不能为空");

        // 检查内容合法性
        if (!isValidCommentContent(content)) {
            throw new BusinessException("评论内容包含非法内容");
        }

        if (!hasCommentPermission(commentId, userId)) {
            throw new BusinessException("没有权限修改此评论");
        }

        Comment comment = commentMapper.getById(commentId);
        if (comment == null) {
            throw new BusinessException("评论不存在");
        }

        // 更新评论内容（直接存储原始内容）
        comment.setContent(content);
        commentMapper.update(comment);
        return comment;
    }

    @Override
    @Transactional
    public boolean deleteComment(Long commentId, Long userId) {
        Assert.notNull(commentId, "评论ID不能为空");
        Assert.notNull(userId, "用户ID不能为空");

        if (!hasCommentPermission(commentId, userId)) {
            throw new BusinessException("没有权限删除此评论");
        }

        return commentMapper.deleteById(commentId) > 0;
    }

    @Override
    public List<Comment> getCommentList(Long targetId, String targetType, Long userId, String orderBy,
                                      Integer page, Integer size, Integer offset) {
        Assert.notNull(page, "页码不能为空");
        Assert.notNull(size, "每页大小不能为空");

        List<Comment> comments = commentMapper.getCommentList(targetId, targetType, userId, null, null, 
                                                           orderBy, page, size, offset);
        
        // 直接返回原始内容
        return comments;
    }

    @Override
    public List<Comment> getReplyList(Long parentId, Integer page, Integer size, Integer offset) {
        Assert.notNull(parentId, "父评论ID不能为空");
        Assert.notNull(page, "页码不能为空");
        Assert.notNull(size, "每页大小不能为空");

        return commentMapper.getCommentList(null, null, null, parentId, 2, 
                                          "time", page, size, offset);
    }

    @Override
    public Long getCommentCount(Long targetId, String targetType, Long userId) {
        Assert.notNull(targetId, "目标ID不能为空");
        Assert.hasText(targetType, "目标类型不能为空");
        return commentMapper.countComments(targetId, targetType, userId);
    }

    @Override
    @Transactional
    public int batchDeleteComments(List<Long> commentIds, Long userId) {
        Assert.notEmpty(commentIds, "评论ID列表不能为空");
        Assert.notNull(userId, "用户ID不能为空");

        // 检查权限
        for (Long commentId : commentIds) {
            if (!hasCommentPermission(commentId, userId)) {
                throw new BusinessException("没有权限删除评论ID: " + commentId);
            }
        }

        return commentMapper.batchDelete(commentIds);
    }

    @Override
    public List<Comment> getUserComments(Long userId, Integer page, Integer size) {
        Assert.notNull(userId, "用户ID不能为空");
        Assert.notNull(page, "页码不能为空");
        Assert.notNull(size, "每页大小不能为空");

        Integer offset = (page - 1) * size;
        List<Comment> comments = commentMapper.getCommentList(null, null, userId, null, null, 
                                                           "time", page, size, offset);
        
        // 直接返回原始内容
        return comments;
    }

    @Override
    public List<Comment> getLatestComments(Integer limit) {
        Assert.notNull(limit, "限制数量不能为空");
        Assert.isTrue(limit > 0, "限制数量必须大于0");

        List<Comment> comments = commentMapper.getLatestComments(limit);
        
        // 直接返回原始内容
        return comments;
    }

    @Override
    public boolean hasCommentPermission(Long commentId, Long userId) {
        Assert.notNull(commentId, "评论ID不能为空");
        Assert.notNull(userId, "用户ID不能为空");

        return commentMapper.isCommentAuthor(commentId, userId);
    }

    /**
     * 检查评论内容是否合法
     */
    private boolean isValidCommentContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            return false;
        }

        // 检查长度限制
        if (content.length() > CONTENT_MAX_LENGTH) {
            return false;
        }
        
        // TODO: 可以添加敏感词检查
        // if (SensitiveWordFilter.containsSensitiveWord(content)) {
        //     return false;
        // }

        return true;
    }

    @Override
    public boolean checkCommentFrequencyLimit(Long userId) {
        String key = COMMENT_FREQUENCY_KEY + userId;
        
        // 获取用户在一分钟内的评论次数
        Integer count = (Integer) redisUtils.get(key);
        if (count == null) {
            // 第一次评论
            redisUtils.set(key, 1, 1, TimeUnit.MINUTES);
            return true;
        }
        
        if (count >= MAX_COMMENTS_PER_MINUTE) {
            return false;
        }
        
        // 增加评论次数
        redisUtils.increment(key, 1);
        return true;
    }

    @Override
    @Transactional
    public int cleanUserComments(Long userId) {
        Assert.notNull(userId, "用户ID不能为空");
        return commentMapper.deleteByUserId(userId);
    }

    @Override
    public List<CommentVO> getCommentListWithReplies(Long targetId, String targetType, Long userId, String orderBy,
                                                     Integer page, Integer size, Integer offset) {
        Assert.notNull(targetId, "目标ID不能为空");
        Assert.hasText(targetType, "目标类型不能为空");
        Assert.notNull(page, "页码不能为空");
        Assert.notNull(size, "每页大小不能为空");

        // 获取一级评论列表
        List<Comment> comments = commentMapper.getCommentList(targetId, targetType, userId, null, 1,
                orderBy, page, size, offset);

        // 转换为VO并获取每个评论的回复
        return comments.stream().map(comment -> {
            CommentVO vo = CommentVO.fromComment(comment);
            // 获取该评论的所有二级回复
            List<Comment> replies = commentMapper.getCommentList(targetId, targetType, null, comment.getId(), 2,
                    "time", 1, 50, 0); // 默认获取50条回复
            vo.setReplies(replies);
            return vo;
        }).collect(Collectors.toList());
    }
}
