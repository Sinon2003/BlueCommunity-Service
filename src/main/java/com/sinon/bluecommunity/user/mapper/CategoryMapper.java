package com.sinon.bluecommunity.user.mapper;

import com.sinon.bluecommunity.common.entity.Category;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 分类数据访问层接口
 */
@Mapper
public interface CategoryMapper {

    /**
     * 插入分类
     */
    int insert(Category category);

    /**
     * 根据ID查询分类
     */
    @Select("SELECT * FROM category WHERE id = #{id}")
    Category selectById(Long id);

    /**
     * 根据名称查询分类
     */
    @Select("SELECT * FROM category WHERE name = #{name}")
    Category selectByName(String name);

    /**
     * 更新分类
     */
    int update(Category category);

    /**
     * 删除分类
     */
    @Delete("DELETE FROM category WHERE id = #{id}")
    int deleteById(Long id);

    /**
     * 获取所有分类
     */
    @Select("SELECT * FROM category ORDER BY sort_order")
    List<Category> selectAll();

    /**
     * 根据状态获取分类列表
     */
    @Select("SELECT * FROM category WHERE status = #{status} ORDER BY sort_order")
    List<Category> listByStatus(Integer status);

    /**
     * 更新分类排序
     */
    @Update("UPDATE category SET sort_order = #{sortOrder} WHERE id = #{id}")
    int updateSortOrder(@Param("id") Long id, @Param("sortOrder") Integer sortOrder);

    /**
     * 更新分类状态
     */
    @Update("UPDATE category SET status = #{status} WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    /**
     * 检查分类名是否存在
     */
    @Select("SELECT COUNT(*) FROM category WHERE name = #{name} AND id != #{excludeId}")
    int checkNameExists(@Param("name") String name, @Param("excludeId") Long excludeId);

    /**
     * 获取分类及其帖子数量
     */
    List<Category> listWithTopicCount();

    /**
     * 批量更新分类状态
     */
    int batchUpdateStatus(@Param("ids") List<Long> ids, @Param("status") Integer status);

    /**
     * 批量更新分类排序
     */
    int batchUpdateSortOrder(@Param("categories") List<Category> categories);

    /**
     * 获取分类下的帖子数量
     */
    @Select("SELECT COUNT(*) FROM topics WHERE category_id = #{categoryId} AND status = 1")
    int getTopicCount(Long categoryId);
}
