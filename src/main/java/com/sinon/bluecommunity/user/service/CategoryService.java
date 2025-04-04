package com.sinon.bluecommunity.user.service;

import com.sinon.bluecommunity.common.entity.Category;

import java.util.List;

/**
 * 分类服务接口
 */
public interface CategoryService {

    /**
     * 创建分类
     *
     * @param category 分类信息
     * @return 创建的分类
     */
    Category createCategory(Category category);

    /**
     * 更新分类
     *
     * @param category 分类信息
     * @return 更新的分类
     */
    Category updateCategory(Category category);

    /**
     * 删除分类
     *
     * @param id 分类ID
     * @return 是否删除成功
     */
    boolean deleteCategory(Long id);

    /**
     * 获取分类详情
     *
     * @param id 分类ID
     * @return 分类详情
     */
    Category getCategoryById(Long id);

    /**
     * 根据名称获取分类
     *
     * @param name 分类名称
     * @return 分类详情
     */
    Category getCategoryByName(String name);

    /**
     * 获取所有分类
     *
     * @return 分类列表
     */
    List<Category> getAllCategories();

    /**
     * 获取启用的分类列表
     *
     * @return 启用的分类列表
     */
    List<Category> getEnabledCategories();

    /**
     * 获取分类及其帖子数量
     *
     * @return 分类及其帖子数量列表
     */
    List<Category> getCategoriesWithTopicCount();

    /**
     * 更新分类排序
     *
     * @param id        分类ID
     * @param sortOrder 排序顺序
     * @return 是否更新成功
     */
    boolean updateSortOrder(Long id, Integer sortOrder);

    /**
     * 更新分类状态
     *
     * @param id     分类ID
     * @param status 状态(0:禁用,1:正常)
     * @return 是否更新成功
     */
    boolean updateStatus(Long id, Integer status);

    /**
     * 批量更新分类状态
     *
     * @param ids    分类ID列表
     * @param status 状态(0:禁用,1:正常)
     * @return 是否更新成功
     */
    boolean batchUpdateStatus(List<Long> ids, Integer status);

    /**
     * 批量更新分类排序
     *
     * @param categories 分类列表（包含id和sortOrder）
     * @return 是否更新成功
     */
    boolean batchUpdateSortOrder(List<Category> categories);

    /**
     * 检查分类名是否存在
     *
     * @param name      分类名
     * @param excludeId 排除的分类ID
     * @return 是否存在
     */
    boolean checkNameExists(String name, Long excludeId);
}
