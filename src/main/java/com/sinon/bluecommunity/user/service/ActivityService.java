package com.sinon.bluecommunity.user.service;

import com.sinon.bluecommunity.common.entity.Activity;
import com.sinon.bluecommunity.common.entity.ActivityParticipant;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 活动服务接口
 */
public interface ActivityService {

    /**
     * 创建活动
     */
    Activity createActivity(Activity activity);

    /**
     * 更新活动
     */
    Activity updateActivity(Activity activity);

    /**
     * 删除活动
     */
    boolean deleteActivity(Long id, Long userId);

    /**
     * 获取活动详情
     */
    Activity getActivityById(Long id);

    /**
     * 获取活动列表
     */
    List<Activity> getActivityList(Long userId, Integer status, String type, String keyword,
                                 LocalDateTime startTimeBegin, LocalDateTime startTimeEnd,
                                 LocalDateTime endTimeBegin, LocalDateTime endTimeEnd,
                                 String locationType, String orderBy,
                                 Integer page, Integer size);

    /**
     * 获取活动总数
     */
    int countActivities(Long userId, Integer status, String type, String keyword,
                       LocalDateTime startTimeBegin, LocalDateTime startTimeEnd,
                       LocalDateTime endTimeBegin, LocalDateTime endTimeEnd,
                       String locationType);

    /**
     * 更新活动状态
     */
    boolean updateStatus(Long id, Integer status);

    /**
     * 批量更新活动状态
     */
    boolean batchUpdateStatus(List<Long> ids, Integer status, Long userId);

    /**
     * 参与活动
     */
    boolean joinActivity(Long activityId, Long userId);

    /**
     * 取消参与活动
     */
    boolean cancelJoinActivity(Long activityId, Long userId);

    /**
     * 获取活动参与者列表
     */
    List<Map<String, Object>> getActivityParticipants(Long activityId, Integer status,
                                                     Integer page, Integer size);

    /**
     * 统计活动参与者数量
     */
    int countActivityParticipants(Long activityId, Integer status);

    /**
     * 更新参与者状态
     */
    boolean updateParticipantStatus(Long activityId, Long userId, Integer status);

    /**
     * 获取用户参与的活动列表
     */
    List<Activity> getUserParticipatedActivities(Long userId, Integer status,
                                               Integer page, Integer size);

    /**
     * 统计用户参与的活动数量
     */
    int countUserParticipatedActivities(Long userId, Integer status);

    /**
     * 获取热门活动
     */
    List<Activity> getHotActivities(String type, String orderBy, Integer limit);

    /**
     * 获取活动统计信息
     */
    Map<String, Object> getActivityStats(LocalDateTime startTime, LocalDateTime endTime, String type);

    /**
     * 获取用户的活动统计信息
     */
    Map<String, Object> getUserActivityStats(Long userId);

    /**
     * 增加活动浏览量
     */
    void incrementViews(Long id);

    /**
     * 点赞活动
     */
    boolean likeActivity(Long id);

    /**
     * 取消点赞活动
     */
    boolean unlikeActivity(Long id);

    /**
     * 检查用户是否已参与活动
     */
    boolean hasJoined(Long activityId, Long userId);

    /**
     * 检查活动是否已满
     */
    boolean isActivityFull(Long id);
}
