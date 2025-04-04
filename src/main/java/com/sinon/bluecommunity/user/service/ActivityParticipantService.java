package com.sinon.bluecommunity.user.service;

import com.sinon.bluecommunity.common.entity.ActivityParticipant;

import java.util.List;
import java.util.Map;

/**
 * 活动参与者服务接口
 */
public interface ActivityParticipantService {

    /**
     * 获取活动的参与者列表
     */
    List<ActivityParticipant> listByActivityId(Long activityId);

    /**
     * 获取活动的特定状态的参与者列表
     */
    List<ActivityParticipant> listByActivityIdAndStatus(Long activityId, Integer status);

    /**
     * 分页获取活动的参与者列表
     */
    List<ActivityParticipant> listByPage(Long activityId, Integer status, Integer page, Integer size);

    /**
     * 获取活动参与者的详细信息（包含用户信息）
     */
    List<Map<String, Object>> listWithUserInfo(Long activityId, Integer status, Integer page, Integer size);

    /**
     * 更新参与状态
     */
    boolean updateStatus(Long id, Integer status);

    /**
     * 更新用户在活动中的状态
     */
    boolean updateStatusByActivityAndUser(Long activityId, Long userId, Integer status);

    /**
     * 批量更新参与状态
     */
    boolean batchUpdateStatus(List<Long> ids, Integer status);

    /**
     * 检查用户是否已参与活动
     */
    ActivityParticipant getByActivityIdAndUserId(Long activityId, Long userId);

    /**
     * 统计活动的参与人数
     */
    int countParticipants(Long activityId, Integer status);

    /**
     * 删除用户的活动参与记录
     */
    boolean deleteByActivityIdAndUserId(Long activityId, Long userId);

    /**
     * 获取用户参与的所有活动ID
     */
    List<Long> getParticipatedActivityIds(Long userId, Integer status);

    /**
     * 添加活动参与记录
     */
    ActivityParticipant addParticipant(ActivityParticipant participant);

    /**
     * 批量添加活动参与记录
     */
    boolean batchAddParticipants(List<ActivityParticipant> participants);

    /**
     * 获取用户参与的活动列表
     */
    List<Map<String, Object>> getUserParticipatedActivities(Long userId, Integer status, Integer page, Integer size);

    /**
     * 统计用户参与的活动数量
     */
    int countUserParticipatedActivities(Long userId, Integer status);
}
