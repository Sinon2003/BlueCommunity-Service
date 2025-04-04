package com.sinon.bluecommunity.user.service;

import com.sinon.bluecommunity.common.entity.Topic;
import com.sinon.bluecommunity.common.vo.PageVO;
import com.sinon.bluecommunity.common.vo.TopicVO;

import java.util.List;

/**
 * 话题服务接口
 */
public interface TopicService {

    /**
     * 创建话题
     * @param topic 话题信息
     * @return 话题ID
     */
    Long createTopic(Topic topic);

    /**
     * 更新话题
     * @param topic 话题信息
     */
    void updateTopic(Topic topic);

    /**
     * 获取话题详情
     * @param id 话题ID
     * @return 话题详情（包含作者信息）
     */
    TopicVO getTopicDetail(Long id);

    /**
     * 分页获取话题列表
     * @param userId 用户ID（可选）
     * @param categoryId 分类ID（可选）
     * @param status 状态（可选）
     * @param keyword 搜索关键词（可选）
     * @param orderBy 排序方式（可选）：views(浏览量), likes(点赞数), comments(评论数)
     * @param page 页码
     * @param size 每页大小
     * @return 分页数据
     */
    PageVO<TopicVO> getTopicList(Long userId, Long categoryId, Integer status,
                                String keyword, String orderBy, Integer page, Integer size);

    /**
     * 删除话题（逻辑删除）
     * @param id 话题ID
     * @param operatorId 操作者ID
     */
    void deleteTopic(Long id, Long operatorId);

    /**
     * 设置话题置顶状态
     * @param id 话题ID
     * @param isPinned 是否置顶
     * @param operatorId 操作者ID
     */
    void updatePinned(Long id, Boolean isPinned, Long operatorId);

    /**
     * 增加话题浏览量
     * @param id 话题ID
     */
    void incrementViews(Long id);

    /**
     * 点赞话题
     * @param topicId 话题ID
     * @param userId 用户ID
     */
    void likeTopic(Long topicId, Long userId);

    /**
     * 取消点赞
     * @param topicId 话题ID
     * @param userId 用户ID
     */
    void unlikeTopic(Long topicId, Long userId);

    /**
     * 检查用户是否有权限操作话题
     * @param userId 用户ID
     * @param topicId 话题ID
     * @return true:有权限 false:无权限
     */
    boolean checkPermission(Long userId, Long topicId);

    /**
     * 获取用户的话题统计
     * @param userId 用户ID
     * @return 统计数据（总数、已发布数、草稿数）
     */
    TopicVO.UserTopicStats getUserTopicStats(Long userId);

    /**
     * 批量更新话题状态
     * @param ids 话题ID列表
     * @param status 目标状态
     * @param operatorId 操作者ID
     */
    void batchUpdateStatus(List<Long> ids, Integer status, Long operatorId);

    /**
     * 获取分类的话题统计
     * @param categoryId 分类ID
     * @return 统计数据（总数、正常状态数）
     */
    TopicVO.CategoryTopicStats getCategoryTopicStats(Long categoryId);
}
