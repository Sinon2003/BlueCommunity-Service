package com.sinon.bluecommunity.user.mapper;

import com.sinon.bluecommunity.common.entity.Topic;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TopicMapper {
    
    /**
     * 插入话题
     */
    int insert(Topic topic);
    
    /**
     * 更新话题
     */
    int update(Topic topic);
    
    /**
     * 根据ID查询话题
     */
    Topic selectById(@Param("id") Long id);
    
    /**
     * 条件查询话题列表
     * @param userId 用户ID
     * @param categoryId 分类ID
     * @param status 状态
     * @param keyword 搜索关键词
     * @param offset 偏移量
     * @param size 每页大小
     * @param orderBy 排序方式：views(浏览量), likes(点赞数), comments(评论数)
     */
    List<Topic> selectList(@Param("userId") Long userId,
                          @Param("categoryId") Long categoryId,
                          @Param("status") Integer status,
                          @Param("keyword") String keyword,
                          @Param("offset") Integer offset,
                          @Param("size") Integer size,
                          @Param("orderBy") String orderBy);
    
    /**
     * 更新话题状态
     */
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
    
    /**
     * 更新话题置顶状态
     */
    int updatePinned(@Param("id") Long id, @Param("isPinned") Boolean isPinned);
    
    /**
     * 增加浏览量
     */
    int incrementViews(@Param("id") Long id);
    
    /**
     * 更新点赞数
     * @param increment 增量，可以为负数
     */
    int updateLikes(@Param("id") Long id, @Param("increment") Integer increment);
    
    /**
     * 更新评论数
     * @param increment 增量，可以为负数
     */
    int updateComments(@Param("id") Long id, @Param("increment") Integer increment);
    
    /**
     * 批量查询话题
     */
    List<Topic> selectByIds(@Param("ids") List<Long> ids);
    
    /**
     * 统计用户的话题数
     */
    int countByUser(@Param("userId") Long userId, @Param("status") Integer status);
    
    /**
     * 统计分类的话题数
     */
    int countByCategory(@Param("categoryId") Long categoryId, @Param("status") Integer status);
    
    /**
     * 批量更新话题状态
     */
    int batchUpdateStatus(@Param("ids") List<Long> ids, @Param("status") Integer status);
    
    /**
     * 统计符合条件的话题总数
     * @param userId 用户ID
     * @param categoryId 分类ID
     * @param status 状态
     * @param keyword 搜索关键词
     * @return 总数
     */
    long count(@Param("userId") Long userId,
              @Param("categoryId") Long categoryId,
              @Param("status") Integer status,
              @Param("keyword") String keyword);
    
    /**
     * 统计用户的发帖数
     */
    @Select("SELECT COUNT(*) FROM topics WHERE user_id = #{userId} AND status != -1")
    Integer countByUserId(@Param("userId") Long userId);
    
    /**
     * 查询热门帖子
     * 
     * @param categoryId 可选的分类ID
     * @param days 统计的时间范围，如近7天、30天
     * @param hotType 热门类型: "comprehensive"(综合), "views"(浏览量), "likes"(点赞), "comments"(评论)
     * @param offset 分页偏移量
     * @param size 分页大小
     * @return 热门帖子列表
     */
    List<Topic> selectHotTopics(
        @Param("categoryId") Long categoryId,
        @Param("days") Integer days,
        @Param("hotType") String hotType,
        @Param("offset") Integer offset,
        @Param("size") Integer size);
        
    /**
     * 统计符合条件的热门帖子总数
     * 
     * @param categoryId 可选的分类ID
     * @param days 统计的时间范围，如近7天、30天
     * @return 总数
     */
    long countHotTopics(
        @Param("categoryId") Long categoryId,
        @Param("days") Integer days);
}
