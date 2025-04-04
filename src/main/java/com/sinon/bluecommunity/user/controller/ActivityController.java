package com.sinon.bluecommunity.user.controller;

import com.sinon.bluecommunity.common.entity.Activity;
import com.sinon.bluecommunity.common.vo.Result;
import com.sinon.bluecommunity.common.utils.ThreadLocalUtil;
import com.sinon.bluecommunity.user.service.ActivityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 活动控制器
 */
@Tag(name = "活动管理", description = "活动相关的API")
@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @Operation(summary = "创建活动")
    @PostMapping("/create")
    public Result<Activity> createActivity(@RequestBody Activity activity) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        if (claims != null && claims.containsKey("userId")) {
            Object userId = claims.get("userId");
            if (userId instanceof Number) {
                activity.setUserId(((Number) userId).longValue());
            } else {
                return Result.error("用户ID无效");
            }
        } else {
            return Result.error("用户未登录");
        }
        activityService.createActivity(activity);
        return Result.success();
    }

    @Operation(summary = "更新活动")
    @PostMapping("/update")
    public Result<Activity> updateActivity(@RequestParam Long id, @RequestBody Activity activity) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        if (claims != null && claims.containsKey("userId")) {
            Object userId = claims.get("userId");
            if (userId instanceof Number) {
                activity.setId(id);
                activity.setUserId(((Number) userId).longValue());
                return Result.success(activityService.updateActivity(activity));
            } else {
                return Result.error("用户ID无效");
            }
        } else {
            return Result.error("用户未登录");
        }
    }

    @Operation(summary = "删除活动")
    @PostMapping("/delete")
    public Result<Boolean> deleteActivity(@RequestParam Long id) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        if (claims != null && claims.containsKey("userId")) {
            Object userId = claims.get("userId");
            if (userId instanceof Number) {
                return Result.success(activityService.deleteActivity(id, ((Number) userId).longValue()));
            } else {
                return Result.error("用户ID无效");
            }
        } else {
            return Result.error("用户未登录");
        }
    }

    @Operation(summary = "获取活动详情")
    @GetMapping("/detail")
    public Result<Activity> getActivity(@RequestParam Long id) {
        activityService.incrementViews(id);
        return Result.success(activityService.getActivityById(id));
    }

    @Operation(summary = "获取活动列表")
    @GetMapping("/list")
    public Result<List<Activity>> getActivityList(
            @Parameter(description = "用户ID") @RequestParam(required = false) Long userId,
            @Parameter(description = "活动状态") @RequestParam(required = false) Integer status,
            @Parameter(description = "活动类型") @RequestParam(required = false) String type,
            @Parameter(description = "关键词") @RequestParam(required = false) String keyword,
            @Parameter(description = "开始时间起始") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTimeBegin,
            @Parameter(description = "开始时间结束") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTimeEnd,
            @Parameter(description = "结束时间起始") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTimeBegin,
            @Parameter(description = "结束时间结束") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTimeEnd,
            @Parameter(description = "地点类型") @RequestParam(required = false) String locationType,
            @Parameter(description = "排序方式") @RequestParam(required = false) String orderBy,
            @Parameter(description = "页码") @RequestParam(required = false) Integer page,
            @Parameter(description = "每页大小") @RequestParam(required = false) Integer size) {
        return Result.success(activityService.getActivityList(
                userId, status, type, keyword,
                startTimeBegin, startTimeEnd, endTimeBegin, endTimeEnd,
                locationType, orderBy, page, size));
    }

    @Operation(summary = "获取活动总数")
    @GetMapping("/count")
    public Result<Integer> countActivities(
            @Parameter(description = "用户ID") @RequestParam(required = false) Long userId,
            @Parameter(description = "活动状态") @RequestParam(required = false) Integer status,
            @Parameter(description = "活动类型") @RequestParam(required = false) String type,
            @Parameter(description = "关键词") @RequestParam(required = false) String keyword,
            @Parameter(description = "开始时间起始") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTimeBegin,
            @Parameter(description = "开始时间结束") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTimeEnd,
            @Parameter(description = "结束时间起始") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTimeBegin,
            @Parameter(description = "结束时间结束") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTimeEnd,
            @Parameter(description = "地点类型") @RequestParam(required = false) String locationType) {
        return Result.success(activityService.countActivities(
                userId, status, type, keyword,
                startTimeBegin, startTimeEnd, endTimeBegin, endTimeEnd,
                locationType));
    }

    @Operation(summary = "更新活动状态")
    @PostMapping("/updateStatus")
    public Result<Boolean> updateStatus(
            @RequestParam Long id,
            @Parameter(description = "新状态") @RequestParam Integer status) {
        return Result.success(activityService.updateStatus(id, status));
    }

    @Operation(summary = "批量更新活动状态")
    @PostMapping("/batchUpdateStatus")
    public Result<Boolean> batchUpdateStatus(
            @Parameter(description = "活动ID列表") @RequestBody List<Long> ids,
            @Parameter(description = "新状态") @RequestParam Integer status) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        if (claims != null && claims.containsKey("userId")) {
            Object userId = claims.get("userId");
            if (userId instanceof Number) {
                return Result.success(activityService.batchUpdateStatus(ids, status, ((Number) userId).longValue()));
            } else {
                return Result.error("用户ID无效");
            }
        } else {
            return Result.error("用户未登录");
        }
    }

    @Operation(summary = "参与活动")
    @PostMapping("/join")
    public Result<Boolean> joinActivity(@RequestParam Long id) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        if (claims != null && claims.containsKey("userId")) {
            Object userId = claims.get("userId");
            if (userId instanceof Number) {
                return Result.success(activityService.joinActivity(id, ((Number) userId).longValue()));
            } else {
                return Result.error("用户ID无效");
            }
        } else {
            return Result.error("用户未登录");
        }
    }

    @Operation(summary = "取消参与活动")
    @PostMapping("/cancelJoin")
    public Result<Boolean> cancelJoinActivity(@RequestParam Long id) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        if (claims != null && claims.containsKey("userId")) {
            Object userId = claims.get("userId");
            if (userId instanceof Number) {
                return Result.success(activityService.cancelJoinActivity(id, ((Number) userId).longValue()));
            } else {
                return Result.error("用户ID无效");
            }
        } else {
            return Result.error("用户未登录");
        }
    }

    @Operation(summary = "获取活动参与者列表")
    @GetMapping("/participants")
    public Result<List<Map<String, Object>>> getActivityParticipants(
            @RequestParam Long id,
            @Parameter(description = "参与状态") @RequestParam(required = false) Integer status,
            @Parameter(description = "页码") @RequestParam(required = false) Integer page,
            @Parameter(description = "每页大小") @RequestParam(required = false) Integer size) {
        return Result.success(activityService.getActivityParticipants(id, status, page, size));
    }

    @Operation(summary = "统计活动参与者数量")
    @GetMapping("/participantsCount")
    public Result<Integer> countActivityParticipants(
            @RequestParam Long id,
            @Parameter(description = "参与状态") @RequestParam(required = false) Integer status) {
        return Result.success(activityService.countActivityParticipants(id, status));
    }

    @Operation(summary = "更新参与者状态")
    @PostMapping("/updateParticipantStatus")
    public Result<Boolean> updateParticipantStatus(
            @RequestParam Long activityId,
            @RequestParam Long userId,
            @Parameter(description = "新状态") @RequestParam Integer status) {
        return Result.success(activityService.updateParticipantStatus(activityId, userId, status));
    }

    @Operation(summary = "获取用户参与的活动列表")
    @GetMapping("/userParticipated")
    public Result<List<Activity>> getUserParticipatedActivities(
            @RequestParam Long userId,
            @Parameter(description = "参与状态") @RequestParam(required = false) Integer status,
            @Parameter(description = "页码") @RequestParam(required = false) Integer page,
            @Parameter(description = "每页大小") @RequestParam(required = false) Integer size) {
        return Result.success(activityService.getUserParticipatedActivities(userId, status, page, size));
    }

    @Operation(summary = "统计用户参与的活动数量")
    @GetMapping("/userParticipatedCount")
    public Result<Integer> countUserParticipatedActivities(
            @RequestParam Long userId,
            @Parameter(description = "参与状态") @RequestParam(required = false) Integer status) {
        return Result.success(activityService.countUserParticipatedActivities(userId, status));
    }

    @Operation(summary = "获取热门活动")
    @GetMapping("/hot")
    public Result<List<Activity>> getHotActivities(
            @Parameter(description = "活动类型") @RequestParam(required = false) String type,
            @Parameter(description = "排序方式") @RequestParam(required = false) String orderBy,
            @Parameter(description = "返回数量") @RequestParam(required = false) Integer limit) {
        return Result.success(activityService.getHotActivities(type, orderBy, limit));
    }

    @Operation(summary = "获取活动统计信息")
    @GetMapping("/stats")
    public Result<Map<String, Object>> getActivityStats(
            @Parameter(description = "开始时间") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @Parameter(description = "结束时间") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @Parameter(description = "活动类型") @RequestParam(required = false) String type) {
        return Result.success(activityService.getActivityStats(startTime, endTime, type));
    }

    @Operation(summary = "获取用户的活动统计信息")
    @GetMapping("/userStats")
    public Result<Map<String, Object>> getUserActivityStats(@RequestParam Long userId) {
        return Result.success(activityService.getUserActivityStats(userId));
    }

    @Operation(summary = "点赞活动")
    @PostMapping("/like")
    public Result<Boolean> likeActivity(@RequestParam Long id) {
        return Result.success(activityService.likeActivity(id));
    }

    @Operation(summary = "取消点赞活动")
    @PostMapping("/unlike")
    public Result<Boolean> unlikeActivity(@RequestParam Long id) {
        return Result.success(activityService.unlikeActivity(id));
    }

    @Operation(summary = "检查用户是否已参与活动")
    @GetMapping("/hasJoined")
    public Result<Boolean> hasJoined(@RequestParam Long id) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        if (claims != null && claims.containsKey("userId")) {
            Object userId = claims.get("userId");
            if (userId instanceof Number) {
                return Result.success(activityService.hasJoined(id, ((Number) userId).longValue()));
            } else {
                return Result.error("用户ID无效");
            }
        } else {
            return Result.error("用户未登录");
        }
    }

    @Operation(summary = "检查活动是否已满")
    @GetMapping("/isFull")
    public Result<Boolean> isActivityFull(@RequestParam Long id) {
        return Result.success(activityService.isActivityFull(id));
    }
}
