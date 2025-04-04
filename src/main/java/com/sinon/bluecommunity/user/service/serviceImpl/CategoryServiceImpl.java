package com.sinon.bluecommunity.user.service.serviceImpl;

import com.sinon.bluecommunity.common.entity.Category;
import com.sinon.bluecommunity.common.exception.BusinessException;
import com.sinon.bluecommunity.user.mapper.CategoryMapper;
import com.sinon.bluecommunity.user.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    @Transactional
    public Category createCategory(Category category) {
        Assert.hasText(category.getName(), "分类名称不能为空");
        
        // 检查分类名是否已存在
        if (checkNameExists(category.getName(), null)) {
            throw new BusinessException("分类名称已存在");
        }

        // 设置默认值
        if (category.getStatus() == null) {
            category.setStatus(1);
        }
        if (category.getSortOrder() == null) {
            category.setSortOrder(0);
        }

        categoryMapper.insert(category);
        return category;
    }

    @Override
    @Transactional
    public Category updateCategory(Category category) {
        Assert.notNull(category.getId(), "分类ID不能为空");
        Assert.hasText(category.getName(), "分类名称不能为空");

        // 检查分类是否存在
        Category existingCategory = categoryMapper.selectById(category.getId());
        if (existingCategory == null) {
            throw new BusinessException("分类不存在");
        }

        // 检查分类名是否已存在（排除自身）
        if (checkNameExists(category.getName(), category.getId())) {
            throw new BusinessException("分类名称已存在");
        }

        categoryMapper.update(category);
        return categoryMapper.selectById(category.getId());
    }

    @Override
    @Transactional
    public boolean deleteCategory(Long id) {
        Assert.notNull(id, "分类ID不能为空");

        // 检查分类是否存在
        Category category = categoryMapper.selectById(id);
        if (category == null) {
            throw new BusinessException("分类不存在");
        }

        // 检查分类下是否有帖子
        int topicCount = categoryMapper.getTopicCount(id);
        if (topicCount > 0) {
            throw new BusinessException("该分类下还有" + topicCount + "个帖子，不能删除");
        }
        
        return categoryMapper.deleteById(id) > 0;
    }

    @Override
    public Category getCategoryById(Long id) {
        Assert.notNull(id, "分类ID不能为空");
        return categoryMapper.selectById(id);
    }

    @Override
    public Category getCategoryByName(String name) {
        Assert.hasText(name, "分类名称不能为空");
        return categoryMapper.selectByName(name);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryMapper.selectAll();
    }

    @Override
    public List<Category> getEnabledCategories() {
        return categoryMapper.listByStatus(1);
    }

    @Override
    public List<Category> getCategoriesWithTopicCount() {
        return categoryMapper.listWithTopicCount();
    }

    @Override
    @Transactional
    public boolean updateSortOrder(Long id, Integer sortOrder) {
        Assert.notNull(id, "分类ID不能为空");
        Assert.notNull(sortOrder, "排序顺序不能为空");

        // 检查分类是否存在
        Category category = categoryMapper.selectById(id);
        if (category == null) {
            throw new BusinessException("分类不存在");
        }

        return categoryMapper.updateSortOrder(id, sortOrder) > 0;
    }

    @Override
    @Transactional
    public boolean updateStatus(Long id, Integer status) {
        Assert.notNull(id, "分类ID不能为空");
        Assert.notNull(status, "状态不能为空");
        Assert.isTrue(status == 0 || status == 1, "状态值无效");

        // 检查分类是否存在
        Category category = categoryMapper.selectById(id);
        if (category == null) {
            throw new BusinessException("分类不存在");
        }

        return categoryMapper.updateStatus(id, status) > 0;
    }

    @Override
    @Transactional
    public boolean batchUpdateStatus(List<Long> ids, Integer status) {
        Assert.notEmpty(ids, "分类ID列表不能为空");
        Assert.notNull(status, "状态不能为空");
        Assert.isTrue(status == 0 || status == 1, "状态值无效");

        return categoryMapper.batchUpdateStatus(ids, status) > 0;
    }

    @Override
    @Transactional
    public boolean batchUpdateSortOrder(List<Category> categories) {
        Assert.notEmpty(categories, "分类列表不能为空");

        // 验证每个分类的ID和排序值
        categories.forEach(category -> {
            Assert.notNull(category.getId(), "分类ID不能为空");
            Assert.notNull(category.getSortOrder(), "排序顺序不能为空");
        });

        return categoryMapper.batchUpdateSortOrder(categories) > 0;
    }

    @Override
    public boolean checkNameExists(String name, Long excludeId) {
        Assert.hasText(name, "分类名称不能为空");
        return categoryMapper.checkNameExists(name, excludeId) > 0;
    }
}
