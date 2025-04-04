package com.sinon.bluecommunity.user.controller;

import com.sinon.bluecommunity.common.dto.ResourceCreateDTO;
import com.sinon.bluecommunity.common.dto.ResourceTypeCountDTO;
import com.sinon.bluecommunity.common.entity.Resource;
import com.sinon.bluecommunity.common.exception.BusinessException;
import com.sinon.bluecommunity.common.vo.Result;
import com.sinon.bluecommunity.common.utils.ThreadLocalUtil;
import com.sinon.bluecommunity.common.vo.PageVO;
import com.sinon.bluecommunity.user.service.ResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 资源控制器
 */
@Tag(name = "资源管理", description = "资源的增删改查接口")
@RestController
@RequestMapping("/api/resources")
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceService resourceService;

    @Operation(summary = "创建资源")
    @PostMapping("/create")
    public Result<Resource> createResource(@RequestBody ResourceCreateDTO dto) {
        if (!dto.isValid()) {
            throw new BusinessException("资源信息不完整");
        }
        Map<String, Object> claims = ThreadLocalUtil.get();
        if (claims != null && claims.containsKey("userId")) {
            Object userId = claims.get("userId");
            if (userId instanceof Number) {
                return Result.success(resourceService.createResource(dto, ((Number) userId).longValue()));
            } else {
                return Result.error("用户ID无效");
            }
        } else {
            return Result.error("用户未登录");
        }
    }

    @Operation(summary = "更新资源")
    @PostMapping("/update")
    public Result<Resource> updateResource(@RequestBody Resource resource) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        if (claims != null && claims.containsKey("userId")) {
            Object userId = claims.get("userId");
            if (userId instanceof Number) {
                resource.setUserId(((Number) userId).longValue());
                return Result.success(resourceService.updateResource(resource));
            } else {
                return Result.error("用户ID无效");
            }
        } else {
            return Result.error("用户未登录");
        }
    }

    @Operation(summary = "删除资源")
    @PostMapping("/delete")
    public Result<Boolean> deleteResource(@RequestParam Long id) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        if (claims != null && claims.containsKey("userId")) {
            Object userId = claims.get("userId");
            if (userId instanceof Number) {
                return Result.success(resourceService.deleteResource(id, ((Number) userId).longValue()));
            } else {
                return Result.error("用户ID无效");
            }
        } else {
            return Result.error("用户未登录");
        }
    }

    @Operation(summary = "获取资源详情")
    @GetMapping("/detail")
    public Result<Resource> getResourceDetail(@RequestParam Long id) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        Long currentUserId = null;
        if (claims != null && claims.containsKey("userId")) {
            Object userId = claims.get("userId");
            if (userId instanceof Number) {
                currentUserId = ((Number) userId).longValue();
            }
        }
        return Result.success(resourceService.getResourceById(id, currentUserId));
    }

    @Operation(summary = "获取资源列表")
    @GetMapping("/list")
    public Result<PageVO<Resource>> getResourceList(
            @Parameter(description = "用户ID") @RequestParam(required = false) Long userId,
            @Parameter(description = "资源类型") @RequestParam(required = false) String type,
            @Parameter(description = "资源状态") @RequestParam(required = false) Integer status,
            @Parameter(description = "搜索关键词") @RequestParam(required = false) String keyword,
            @Parameter(description = "排序字段") @RequestParam(required = false) String orderBy,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size) {
        
        Map<String, Object> claims = ThreadLocalUtil.get();
        Long currentUserId = null;
        if (claims != null && claims.containsKey("userId")) {
            Object userIdObj = claims.get("userId");
            if (userIdObj instanceof Number) {
                currentUserId = ((Number) userIdObj).longValue();
            }
        }
        
        return Result.success(resourceService.getResourceList(userId, type, status, keyword, 
                orderBy, page, size, currentUserId));
    }

    @Operation(summary = "获取热门资源")
    @GetMapping("/hot")
    public Result<List<Resource>> getHotResources(
            @Parameter(description = "返回数量") @RequestParam(defaultValue = "10") Integer limit) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        Long currentUserId = null;
        if (claims != null && claims.containsKey("userId")) {
            Object userId = claims.get("userId");
            if (userId instanceof Number) {
                currentUserId = ((Number) userId).longValue();
            }
        }
        return Result.success(resourceService.getHotResources(limit, currentUserId));
    }

    @Operation(summary = "按类型获取资源")
    @GetMapping("/type/{type}")
    public Result<List<Resource>> getResourcesByType(
            @Parameter(description = "资源类型") @PathVariable String type,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        Long currentUserId = null;
        if (claims != null && claims.containsKey("userId")) {
            Object userId = claims.get("userId");
            if (userId instanceof Number) {
                currentUserId = ((Number) userId).longValue();
            }
        }
        return Result.success(resourceService.getResourcesByType(type, page, size, currentUserId));
    }

    @Operation(summary = "获取用户上传的资源")
    @GetMapping("/user/{userId}")
    public Result<List<Resource>> getUserResources(
            @Parameter(description = "用户ID") @PathVariable Long userId,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        Long currentUserId = null;
        if (claims != null && claims.containsKey("userId")) {
            Object userIdObj = claims.get("userId");
            if (userIdObj instanceof Number) {
                currentUserId = ((Number) userIdObj).longValue();
            }
        }
        return Result.success(resourceService.getUserResources(userId, page, size, currentUserId));
    }

    @Operation(summary = "统计各类型资源数量")
    @GetMapping("/count/type")
    public Result<List<ResourceTypeCountDTO>> countResourcesByType() {
        return Result.success(resourceService.countResourcesByType());
    }

    @Operation(summary = "点赞资源")
    @PostMapping("/like")
    public Result<Boolean> likeResource(@RequestParam Long id) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        if (claims != null && claims.containsKey("userId")) {
            Object userId = claims.get("userId");
            if (userId instanceof Number) {
                return Result.success(resourceService.likeResource(id, ((Number) userId).longValue()));
            } else {
                return Result.error("用户ID无效");
            }
        } else {
            return Result.error("用户未登录");
        }
    }

    @Operation(summary = "取消点赞")
    @PostMapping("/unlike")
    public Result<Boolean> unlikeResource(@RequestParam Long id) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        if (claims != null && claims.containsKey("userId")) {
            Object userId = claims.get("userId");
            if (userId instanceof Number) {
                return Result.success(resourceService.unlikeResource(id, ((Number) userId).longValue()));
            } else {
                return Result.error("用户ID无效");
            }
        } else {
            return Result.error("用户未登录");
        }
    }

    @Operation(summary = "下载资源")
    @PostMapping("/download")
    public Result<Boolean> downloadResource(@RequestParam Long id) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        if (claims != null && claims.containsKey("userId")) {
            Object userId = claims.get("userId");
            if (userId instanceof Number) {
                return Result.success(resourceService.downloadResource(id, ((Number) userId).longValue()));
            } else {
                return Result.error("用户ID无效");
            }
        } else {
            return Result.error("用户未登录");
        }
    }

    @Operation(summary = "批量更新资源状态")
    @PostMapping("/batch/status")
    public Result<Boolean> batchUpdateStatus(
            @Parameter(description = "资源ID列表") @RequestParam List<Long> ids,
            @Parameter(description = "目标状态") @RequestParam Integer status) {
        return Result.success(resourceService.batchUpdateStatus(ids, status));
    }

    @Operation(summary = "审核资源")
    @PostMapping("/review")
    public Result<Boolean> reviewResource(
            @Parameter(description = "资源ID") @RequestParam Long id,
            @Parameter(description = "审核状态") @RequestParam Integer status,
            @Parameter(description = "审核意见") @RequestParam(required = false) String reason) {
        return Result.success(resourceService.reviewResource(id, status, reason));
    }
}
