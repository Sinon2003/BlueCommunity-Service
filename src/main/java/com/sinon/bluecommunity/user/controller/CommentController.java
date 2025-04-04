package com.sinon.bluecommunity.user.controller;

import com.sinon.bluecommunity.common.entity.Comment;
import com.sinon.bluecommunity.common.utils.ThreadLocalUtil;
import com.sinon.bluecommunity.common.vo.CommentVO;
import com.sinon.bluecommunity.common.vo.Result;
import com.sinon.bluecommunity.user.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 评论控制器
 */
@RestController
@RequestMapping("/api/comments")
@Validated
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 发表评论
     */
    @PostMapping("/create")
    public Result<Comment> publishComment(@RequestBody @Validated Map<String, Object> requestBody) {
        Long targetId = Long.valueOf(requestBody.get("targetId").toString());
        String targetType = (String) requestBody.get("targetType");
        String content = (String) requestBody.get("content");
        Map<String, Object> claims = ThreadLocalUtil.get();
        Long userId = Long.valueOf(claims.get("userId").toString());

        Comment comment = commentService.publishComment(targetId, targetType, userId, content);
        return Result.success(comment);
    }

    /**
     * 更新评论
     */
    @PutMapping("/update")
    public Result<Comment> updateComment(@RequestParam Long commentId,
                                       @RequestBody Map<String, String> requestBody) {
        String content = requestBody.get("content");
        Map<String, Object> claims = ThreadLocalUtil.get();
        Long userId = Long.valueOf(claims.get("userId").toString());

        Comment comment = commentService.updateComment(commentId, userId, content);
        return Result.success(comment);
    }

    /**
     * 获取评论列表（包含回复）
     * @param targetId 目标ID
     * @param targetType 目标类型
     * @param orderBy 排序方式：created_at（默认，按创建时间倒序）, hot（按热度倒序）
     * @param page 页码，默认1
     * @param size 每页大小，默认10，最大50
     * @return 评论列表（包含回复）
     */
    @GetMapping("/list")
    public Result<List<CommentVO>> getCommentList(
            @RequestParam Long targetId,
            @RequestParam String targetType,
            @RequestParam(required = false, defaultValue = "created_at") String orderBy,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        // 参数验证
        if (page < 1) {
            page = 1;
        }
        if (size < 1) {
            size = 10;
        }
        if (size > 50) {
            size = 50;
        }
        // 验证orderBy参数
        if (!orderBy.equals("created_at") && !orderBy.equals("hot")) {
            orderBy = "created_at";
        }
        
        Map<String, Object> claims = ThreadLocalUtil.get();
        Long userId = claims != null ? Long.valueOf(claims.get("userId").toString()) : null;
        
        // 计算offset
        int offset = (page - 1) * size;
        List<CommentVO> comments = commentService.getCommentListWithReplies(targetId, targetType, userId, orderBy, page, size, offset);
        return Result.success(comments);
    }

    /**
     * 删除评论
     */
    @DeleteMapping("/delete")
    public Result<Boolean> deleteComment(@RequestParam Long commentId) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        Long userId = Long.valueOf(claims.get("userId").toString());

        boolean success = commentService.deleteComment(commentId, userId);
        return Result.success(success);
    }

    /**
     * 获取评论数量
     */
    @GetMapping("/count")
    public Result<Long> getCommentCount(
            @RequestParam Long targetId,
            @RequestParam String targetType) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        Long userId = claims != null ? Long.valueOf(claims.get("userId").toString()) : null;

        Long count = commentService.getCommentCount(targetId, targetType, userId);
        return Result.success(count);
    }

    /**
     * 获取用户的评论列表
     */
    @GetMapping("/user/list")
    public Result<List<Comment>> getUserComments(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        Long userId = Long.valueOf(claims.get("userId").toString());

        List<Comment> comments = commentService.getUserComments(userId, page, size);
        return Result.success(comments);
    }

    /**
     * 批量删除评论
     */
    @DeleteMapping("/batch/delete")
    public Result<Void> batchDeleteComments(@RequestParam List<Long> commentIds) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        Long userId = Long.valueOf(claims.get("userId").toString());

        commentService.batchDeleteComments(commentIds, userId);
        return Result.success();
    }

    /**
     * 发表回复
     */
    @PostMapping("/reply")
    public Result<Comment> publishReply(@RequestBody @Validated Map<String, Object> requestBody) {
        Long targetId = Long.valueOf(requestBody.get("targetId").toString());
        String targetType = (String) requestBody.get("targetType");
        Long parentId = Long.valueOf(requestBody.get("parentId").toString());
        Long replyUserId = Long.valueOf(requestBody.get("replyUserId").toString());
        String content = (String) requestBody.get("content");
        
        Map<String, Object> claims = ThreadLocalUtil.get();
        Long userId = Long.valueOf(claims.get("userId").toString());

        commentService.publishReply(targetId, targetType, parentId, userId, replyUserId, content);
        return Result.success();
    }

    /**
     * 获取二级回复列表
     * @param parentId 父评论ID
     * @param page 页码，默认1
     * @param size 每页大小，默认10，最大50
     * @return 二级回复列表
     */
    @GetMapping("/replies")
    public Result<List<Comment>> getReplyList(
            @RequestParam Long parentId,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        // 参数验证
        if (page < 1) {
            page = 1;
        }
        if (size < 1) {
            size = 10;
        }
        if (size > 50) {
            size = 50;
        }
        
        // 计算offset
        int offset = (page - 1) * size;
        List<Comment> replies = commentService.getReplyList(parentId, page, size, offset);
        return Result.success(replies);
    }
}
