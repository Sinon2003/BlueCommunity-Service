package com.sinon.bluecommunity.user.controller;

import com.sinon.bluecommunity.common.entity.ActivityParticipant;
import com.sinon.bluecommunity.common.vo.Result;
import com.sinon.bluecommunity.common.utils.ThreadLocalUtil;
import com.sinon.bluecommunity.user.service.ActivityParticipantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 活动参与者控制器
 */
@Tag(name = "活动参与管理", description = "活动参与相关的API")
@RestController
@RequestMapping("/api/activity/participants")
public class ActivityParticipantController {

    @Autowired
    private ActivityParticipantService activityParticipantService;

    @Operation(summary = "获取活动的参与者列表")
    @GetMapping("/list")
    public Result<List<ActivityParticipant>> listParticipants(
            @Parameter(description = "活动ID") @RequestParam Long activityId) {
        return Result.success(activityParticipantService.listByActivityId(activityId));
    }

    @Operation(summary = "获取活动的特定状态的参与者列表")
    @GetMapping("/listByStatus")
    public Result<List<ActivityParticipant>> listParticipantsByStatus(
            @Parameter(description = "活动ID") @RequestParam Long activityId,
            @Parameter(description = "参与状态") @RequestParam Integer status) {
        return Result.success(activityParticipantService.listByActivityIdAndStatus(activityId, status));
    }

    @Operation(summary = "分页获取活动的参与者列表")
    @GetMapping("/page")
    public Result<List<ActivityParticipant>> listParticipantsByPage(
            @Parameter(description = "活动ID") @RequestParam Long activityId,
            @Parameter(description = "参与状态") @RequestParam(required = false) Integer status,
            @Parameter(description = "页码") @RequestParam(required = false) Integer page,
            @Parameter(description = "每页大小") @RequestParam(required = false) Integer size) {
        return Result.success(activityParticipantService.listByPage(activityId, status, page, size));
    }

    @Operation(summary = "获取活动参与者的详细信息")
    @GetMapping("/detail")
    public Result<List<Map<String, Object>>> listParticipantsWithUserInfo(
            @Parameter(description = "活动ID") @RequestParam Long activityId,
            @Parameter(description = "参与状态") @RequestParam(required = false) Integer status,
            @Parameter(description = "页码") @RequestParam(required = false) Integer page,
            @Parameter(description = "每页大小") @RequestParam(required = false) Integer size) {
        return Result.success(activityParticipantService.listWithUserInfo(activityId, status, page, size));
    }

    @Operation(summary = "更新参与状态")
    @PostMapping("/updateStatus")
    public Result<Boolean> updateStatus(
            @Parameter(description = "记录ID") @RequestParam Long id,
            @Parameter(description = "新状态") @RequestParam Integer status) {
        return Result.success(activityParticipantService.updateStatus(id, status));
    }

    @Operation(summary = "更新用户在活动中的状态")
    @PostMapping("/updateUserStatus")
    public Result<Boolean> updateUserStatus(
            @Parameter(description = "活动ID") @RequestParam Long activityId,
            @Parameter(description = "用户ID") @RequestParam Long userId,
            @Parameter(description = "新状态") @RequestParam Integer status) {
        return Result.success(activityParticipantService.updateStatusByActivityAndUser(activityId, userId, status));
    }

    @Operation(summary = "批量更新参与状态")
    @PostMapping("/batchUpdateStatus")
    public Result<Boolean> batchUpdateStatus(
            @Parameter(description = "记录ID列表") @RequestBody List<Long> ids,
            @Parameter(description = "新状态") @RequestParam Integer status) {
        return Result.success(activityParticipantService.batchUpdateStatus(ids, status));
    }

    @Operation(summary = "检查用户是否已参与活动")
    @GetMapping("/check")
    public Result<ActivityParticipant> checkParticipation(
            @Parameter(description = "活动ID") @RequestParam Long activityId) {
        return Result.success(activityParticipantService.getByActivityIdAndUserId(activityId, ThreadLocalUtil.get()));
    }

    @Operation(summary = "统计活动的参与人数")
    @GetMapping("/count")
    public Result<Integer> countParticipants(
            @Parameter(description = "活动ID") @RequestParam Long activityId,
            @Parameter(description = "参与状态") @RequestParam(required = false) Integer status) {
        return Result.success(activityParticipantService.countParticipants(activityId, status));
    }

    @Operation(summary = "取消参与活动")
    @PostMapping("/cancel")
    public Result<Boolean> cancelParticipation(
            @Parameter(description = "活动ID") @RequestParam Long activityId) {
        return Result.success(activityParticipantService.deleteByActivityIdAndUserId(activityId, ThreadLocalUtil.get()));
    }

    @Operation(summary = "获取用户参与的所有活动ID")
    @GetMapping("/participatedIds")
    public Result<List<Long>> getParticipatedActivityIds(
            @Parameter(description = "参与状态") @RequestParam(required = false) Integer status) {
        return Result.success(activityParticipantService.getParticipatedActivityIds(ThreadLocalUtil.get(), status));
    }

    @Operation(summary = "参与活动")
    @PostMapping("/join")
    public Result<ActivityParticipant> joinActivity(
            @Parameter(description = "活动ID") @RequestParam Long activityId) {
        ActivityParticipant participant = new ActivityParticipant();
        participant.setActivityId(activityId);
        participant.setUserId(ThreadLocalUtil.get());
        participant.setStatus(1); // 默认状态为已确认
        return Result.success(activityParticipantService.addParticipant(participant));
    }

    @Operation(summary = "批量添加参与者")
    @PostMapping("/batchAdd")
    public Result<Boolean> batchAddParticipants(@RequestBody List<ActivityParticipant> participants) {
        return Result.success(activityParticipantService.batchAddParticipants(participants));
    }

    @Operation(summary = "获取用户参与的活动列表")
    @GetMapping("/userParticipated")
    public Result<List<Map<String, Object>>> getUserParticipatedActivities(
            @Parameter(description = "用户ID") @RequestParam(required = false) Long userId,
            @Parameter(description = "参与状态") @RequestParam(required = false) Integer status,
            @Parameter(description = "页码") @RequestParam(required = false) Integer page,
            @Parameter(description = "每页大小") @RequestParam(required = false) Integer size) {
        if (userId == null) {
            userId = ThreadLocalUtil.get();
        }
        return Result.success(activityParticipantService.getUserParticipatedActivities(userId, status, page, size));
    }

    @Operation(summary = "统计用户参与的活动数量")
    @GetMapping("/userParticipatedCount")
    public Result<Integer> countUserParticipatedActivities(
            @Parameter(description = "用户ID") @RequestParam(required = false) Long userId,
            @Parameter(description = "参与状态") @RequestParam(required = false) Integer status) {
        if (userId == null) {
            userId = ThreadLocalUtil.get();
        }
        return Result.success(activityParticipantService.countUserParticipatedActivities(userId, status));
    }
}
