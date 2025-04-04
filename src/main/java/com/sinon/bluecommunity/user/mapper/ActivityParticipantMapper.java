package com.sinon.bluecommunity.user.mapper;

import com.sinon.bluecommunity.common.entity.ActivityParticipant;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * 活动参与者数据访问层接口
 */
@Mapper
public interface ActivityParticipantMapper {
    
    /**
     * 获取活动的参与者列表
     */
    List<ActivityParticipant> listByActivityId(Long activityId);

    /**
     * 获取活动的特定状态的参与者列表
     */
    List<ActivityParticipant> listByActivityIdAndStatus(@Param("activityId") Long activityId, @Param("status") Integer status);

    /**
     * 分页获取活动的参与者列表
     */
    List<ActivityParticipant> listByPage(@Param("activityId") Long activityId,
                                        @Param("status") Integer status,
                                        @Param("offset") Integer offset,
                                        @Param("limit") Integer limit);

    /**
     * 获取活动参与者的详细信息（包含用户信息）
     */
    List<Map<String, Object>> listWithUserInfo(@Param("activityId") Long activityId,
                                             @Param("status") Integer status,
                                             @Param("offset") Integer offset,
                                             @Param("limit") Integer limit);

    /**
     * 更新参与状态
     */
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    /**
     * 更新用户在活动中的状态
     */
    int updateStatusByActivityAndUser(@Param("activityId") Long activityId,
                                    @Param("userId") Long userId,
                                    @Param("status") Integer status);

    /**
     * 批量更新参与状态
     */
    int batchUpdateStatus(@Param("ids") List<Long> ids, @Param("status") Integer status);

    /**
     * 检查用户是否已参与活动
     */
    ActivityParticipant getByActivityIdAndUserId(@Param("activityId") Long activityId, @Param("userId") Long userId);

    /**
     * 统计活动的参与人数
     */
    int countParticipants(@Param("activityId") Long activityId, @Param("status") Integer status);

    /**
     * 删除用户的活动参与记录
     */
    int deleteByActivityIdAndUserId(@Param("activityId") Long activityId, @Param("userId") Long userId);

    /**
     * 获取用户参与的所有活动ID
     */
    List<Long> getParticipatedActivityIds(@Param("userId") Long userId, @Param("status") Integer status);

    /**
     * 添加活动参与记录
     */
    int insert(ActivityParticipant participant);

    /**
     * 批量添加活动参与记录
     */
    int batchInsert(@Param("participants") List<ActivityParticipant> participants);

    /**
     * 获取用户参与的活动列表
     */
    List<Map<String, Object>> getUserParticipatedActivities(@Param("userId") Long userId,
                                                          @Param("status") Integer status,
                                                          @Param("offset") Integer offset,
                                                          @Param("limit") Integer limit);

    /**
     * 统计用户参与的活动数量
     */
    int countUserParticipatedActivities(@Param("userId") Long userId, @Param("status") Integer status);
}

