package com.sinon.bluecommunity.user.controller;

import com.sinon.bluecommunity.common.entity.Like;
import com.sinon.bluecommunity.common.utils.ThreadLocalUtil;
import com.sinon.bluecommunity.common.vo.Result;
import com.sinon.bluecommunity.user.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 点赞控制器
 */
@Tag(name = "点赞接口")
@RestController
@RequestMapping("/api/likes")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @Operation(summary = "点赞")
    @PostMapping
    public Result<Void> like(
            @Parameter(description = "目标ID", required = true)
            @RequestParam Long targetId,
            @Parameter(description = "目标类型(topic/activity/resource/comment)", required = true)
            @RequestParam String targetType) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        Long userId = Long.valueOf(claims.get("userId").toString());
        likeService.like(userId, targetId, targetType);
        return Result.success();
    }

    @Operation(summary = "取消点赞")
    @DeleteMapping
    public Result<Void> unlike(
            @Parameter(description = "目标ID", required = true)
            @RequestParam Long targetId,
            @Parameter(description = "目标类型", required = true)
            @RequestParam String targetType) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        Long userId = Long.valueOf(claims.get("userId").toString());
        likeService.unlike(userId, targetId, targetType);
        return Result.success();
    }

    @Operation(summary = "检查是否已点赞")
    @GetMapping("/check")
    public Result<Boolean> checkLikeStatus(
            @Parameter(description = "目标ID", required = true)
            @RequestParam Long targetId,
            @Parameter(description = "目标类型", required = true)
            @RequestParam String targetType) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        Long userId = Long.valueOf(claims.get("userId").toString());
        boolean hasLiked = likeService.hasLiked(userId, targetId, targetType);
        return Result.success(hasLiked);
    }

    @Operation(summary = "获取目标的点赞数")
    @GetMapping("/count")
    public Result<Integer> getLikeCount(
            @Parameter(description = "目标ID", required = true)
            @RequestParam Long targetId,
            @Parameter(description = "目标类型", required = true)
            @RequestParam String targetType) {
        int count = likeService.getLikeCount(targetId, targetType);
        return Result.success(count);
    }

    @Operation(summary = "获取用户的点赞列表")
    @GetMapping("/user")
    public Result<List<Like>> getUserLikes(
            @Parameter(description = "目标类型", required = false)
            @RequestParam(required = false) String targetType,
            @Parameter(description = "页码", required = false)
            @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页大小", required = false)
            @RequestParam(defaultValue = "10") Integer size) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        Long userId = Long.valueOf(claims.get("userId").toString());
        List<Like> likes = likeService.getUserLikes(userId, targetType, page, size);
        return Result.success(likes);
    }
}
