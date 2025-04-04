package com.sinon.bluecommunity.user.controller;

import com.sinon.bluecommunity.common.entity.Follow;
import com.sinon.bluecommunity.common.utils.ThreadLocalUtil;
import com.sinon.bluecommunity.common.vo.PageVO;
import com.sinon.bluecommunity.common.vo.Result;
import com.sinon.bluecommunity.user.service.FollowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 关注控制器
 */
@Tag(name = "关注管理", description = "关注相关接口")
@RestController
@RequestMapping("/api/follows")
public class FollowController {

    @Autowired
    private FollowService followService;

    @Operation(summary = "关注用户")
    @PostMapping("/follow")
    public Result<Void> follow(@RequestParam Long followeeId) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        Long userId = Long.valueOf(claims.get("userId").toString());
        followService.follow(userId, followeeId);
        return Result.success();
    }

    @Operation(summary = "取消关注")
    @PostMapping("/unfollow")
    public Result<Void> unfollow(@RequestParam Long followeeId) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        Long userId = Long.valueOf(claims.get("userId").toString());
        followService.unfollow(userId, followeeId);
        return Result.success();
    }

    @Operation(summary = "检查是否已关注")
    @GetMapping("/check")
    public Result<Boolean> hasFollowed(@RequestParam Long followeeId) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        Long userId = Long.valueOf(claims.get("userId").toString());
        boolean hasFollowed = followService.hasFollowed(userId, followeeId);
        return Result.success(hasFollowed);
    }

    @Operation(summary = "检查是否互相关注")
    @GetMapping("/mutual")
    public Result<Boolean> isMutualFollow(@RequestParam Long targetUserId) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        Long userId = Long.valueOf(claims.get("userId").toString());
        boolean isMutual = followService.isMutualFollow(userId, targetUserId);
        return Result.success(isMutual);
    }

    @Operation(summary = "获取用户的关注列表")
    @GetMapping("/following")
    public Result<PageVO<Follow>> getFollowing(
            @RequestParam(required = false) Long userId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        if (userId == null) {
            Map<String, Object> claims = ThreadLocalUtil.get();
            userId = Long.valueOf(claims.get("userId").toString());
        }
        PageVO<Follow> pageVO = followService.getFollowing(userId, page, size);
        return Result.success(pageVO);
    }

    @Operation(summary = "获取用户的粉丝列表")
    @GetMapping("/followers")
    public Result<PageVO<Follow>> getFollowers(
            @RequestParam(required = false) Long userId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        if (userId == null) {
            Map<String, Object> claims = ThreadLocalUtil.get();
            userId = Long.valueOf(claims.get("userId").toString());
        }
        PageVO<Follow> pageVO = followService.getFollowers(userId, page, size);
        return Result.success(pageVO);
    }

    @Operation(summary = "获取用户的关注数")
    @GetMapping("/following/count")
    public Result<Long> getFollowingCount(@RequestParam(required = false) Long userId) {
        if (userId == null) {
            Map<String, Object> claims = ThreadLocalUtil.get();
            userId = Long.valueOf(claims.get("userId").toString());
        }
        Long count = followService.getFollowingCount(userId);
        return Result.success(count);
    }

    @Operation(summary = "获取用户的粉丝数")
    @GetMapping("/followers/count")
    public Result<Long> getFollowersCount(@RequestParam(required = false) Long userId) {
        if (userId == null) {
            Map<String, Object> claims = ThreadLocalUtil.get();
            userId = Long.valueOf(claims.get("userId").toString());
        }
        Long count = followService.getFollowersCount(userId);
        return Result.success(count);
    }

    @Operation(summary = "批量获取关注状态")
    @PostMapping("/status/batch")
    public Result<List<Follow>> getFollowStatus(@RequestBody List<Long> followeeIds) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        Long userId = Long.valueOf(claims.get("userId").toString());
        List<Follow> follows = followService.getFollowStatus(userId, followeeIds);
        return Result.success(follows);
    }
}
