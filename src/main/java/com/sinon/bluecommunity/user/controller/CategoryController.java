package com.sinon.bluecommunity.user.controller;

import com.sinon.bluecommunity.common.entity.Category;
import com.sinon.bluecommunity.common.vo.Result;
import com.sinon.bluecommunity.user.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * User 的 Category(板块分类) 控制器
 * 增加板块只能在管理员后台操作
 */
@Tag(name = "分类管理", description = "分类相关接口")
@RestController
@RequestMapping("/api/categories")
@Validated
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Operation(summary = "创建分类", security = @SecurityRequirement(name = "Authorization"))
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Category> createCategory(@RequestBody @Valid Category category) {
        Category created = categoryService.createCategory(category);
        return Result.success(created);
    }

    @Operation(summary = "更新分类", security = @SecurityRequirement(name = "Authorization"))
    @PostMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Category> updateCategory(
            @Parameter(description = "分类ID") @RequestParam @NotNull Long id,
            @RequestBody @Valid Category category) {
        category.setId(id);
        Category updated = categoryService.updateCategory(category);
        return Result.success(updated);
    }

    @Operation(summary = "删除分类", security = @SecurityRequirement(name = "Authorization"))
    @PostMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Boolean> deleteCategory(
            @Parameter(description = "分类ID") @RequestParam @NotNull Long id) {
        boolean success = categoryService.deleteCategory(id);
        return Result.success(success);
    }

    @Operation(summary = "获取分类详情")
    @GetMapping("/detail")
    public Result<Category> getCategoryById(
            @Parameter(description = "分类ID") @RequestParam @NotNull Long id) {
        Category category = categoryService.getCategoryById(id);
        return Result.success(category);
    }

    @Operation(summary = "根据名称获取分类")
    @GetMapping("/by-name")
    public Result<Category> getCategoryByName(
            @Parameter(description = "分类名称") @RequestParam @NotEmpty String name) {
        Category category = categoryService.getCategoryByName(name);
        return Result.success(category);
    }

    @Operation(summary = "获取所有分类")
    @GetMapping("/list")
    public Result<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return Result.success(categories);
    }

    @Operation(summary = "获取启用的分类列表")
    @GetMapping("/enabled")
    public Result<List<Category>> getEnabledCategories() {
        List<Category> categories = categoryService.getEnabledCategories();
        return Result.success(categories);
    }

    @Operation(summary = "获取分类及其帖子数量")
    @GetMapping("/with-topic-count")
    public Result<List<Category>> getCategoriesWithTopicCount() {
        List<Category> categories = categoryService.getCategoriesWithTopicCount();
        return Result.success(categories);
    }

    @Operation(summary = "更新分类排序", security = @SecurityRequirement(name = "Authorization"))
    @PostMapping("/update-sort")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Boolean> updateSortOrder(
            @Parameter(description = "分类ID") @RequestParam @NotNull Long id,
            @Parameter(description = "排序顺序") @RequestParam @NotNull Integer sortOrder) {
        boolean success = categoryService.updateSortOrder(id, sortOrder);
        return Result.success(success);
    }

    @Operation(summary = "更新分类状态", security = @SecurityRequirement(name = "Authorization"))
    @PostMapping("/update-status")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Boolean> updateStatus(
            @Parameter(description = "分类ID") @RequestParam @NotNull Long id,
            @Parameter(description = "状态(0:禁用,1:正常)") @RequestParam @NotNull Integer status) {
        boolean success = categoryService.updateStatus(id, status);
        return Result.success(success);
    }

    @Operation(summary = "批量更新分类状态", security = @SecurityRequirement(name = "Authorization"))
    @PostMapping("/batch-update-status")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Boolean> batchUpdateStatus(
            @Parameter(description = "分类ID列表") @RequestBody @NotEmpty List<Long> ids,
            @Parameter(description = "状态(0:禁用,1:正常)") @RequestParam @NotNull Integer status) {
        boolean success = categoryService.batchUpdateStatus(ids, status);
        return Result.success(success);
    }

    @Operation(summary = "批量更新分类排序", security = @SecurityRequirement(name = "Authorization"))
    @PostMapping("/batch-update-sort")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Boolean> batchUpdateSortOrder(
            @Parameter(description = "分类列表") @RequestBody @NotEmpty List<Category> categories) {
        boolean success = categoryService.batchUpdateSortOrder(categories);
        return Result.success(success);
    }

    @Operation(summary = "检查分类名是否存在")
    @GetMapping("/check-name")
    public Result<Boolean> checkNameExists(
            @Parameter(description = "分类名") @RequestParam @NotEmpty String name,
            @Parameter(description = "排除的分类ID") @RequestParam(required = false) Long excludeId) {
        boolean exists = categoryService.checkNameExists(name, excludeId);
        return Result.success(exists);
    }
}
