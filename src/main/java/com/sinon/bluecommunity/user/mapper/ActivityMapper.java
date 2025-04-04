package com.sinon.bluecommunity.user.mapper;

import com.sinon.bluecommunity.common.entity.Activity;
import com.sinon.bluecommunity.common.entity.ActivityParticipant;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 活动数据访问层接口
 */
@Mapper
public interface ActivityMapper {
    
    /**
     * 插入活动
     */
    @Insert("INSERT INTO activities (user_id, title, type, description, cover_url, start_time, end_time, " +
            "location_type, location, max_participants, notice, status) " +
            "VALUES (#{userId}, #{title}, #{type}, #{description}, #{coverUrl}, #{startTime}, #{endTime}, " +
            "#{locationType}, #{location}, #{maxParticipants}, #{notice}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Activity activity);

    /**
     * 更新活动
     */
    @Update("UPDATE activities SET title = #{title}, type = #{type}, description = #{description}, " +
            "cover_url = #{coverUrl}, start_time = #{startTime}, end_time = #{endTime}, " +
            "location_type = #{locationType}, location = #{location}, max_participants = #{maxParticipants}, " +
            "notice = #{notice}, status = #{status} " +
            "WHERE id = #{id} AND user_id = #{userId}")
    int update(Activity activity);

    /**
     * 根据ID获取活动
     */
    @Select("SELECT * FROM activities WHERE id = #{id}")
    Activity selectById(Long id);

    /**
     * 删除活动
     */
    @Delete("DELETE FROM activities WHERE id = #{id} AND user_id = #{userId}")
    int deleteById(@Param("id") Long id, @Param("userId") Long userId);

    /**
     * 获取活动列表
     */
    List<Activity> getActivityList(@Param("userId") Long userId,
                                 @Param("status") Integer status,
                                 @Param("type") String type,
                                 @Param("keyword") String keyword,
                                 @Param("startTimeBegin") LocalDateTime startTimeBegin,
                                 @Param("startTimeEnd") LocalDateTime startTimeEnd,
                                 @Param("endTimeBegin") LocalDateTime endTimeBegin,
                                 @Param("endTimeEnd") LocalDateTime endTimeEnd,
                                 @Param("locationType") String locationType,
                                 @Param("orderBy") String orderBy,
                                 @Param("offset") Integer offset,
                                 @Param("size") Integer size);

    /**
     * 统计活动数量
     */
    int countActivities(@Param("userId") Long userId,
                       @Param("status") Integer status,
                       @Param("type") String type,
                       @Param("keyword") String keyword,
                       @Param("startTimeBegin") LocalDateTime startTimeBegin,
                       @Param("startTimeEnd") LocalDateTime startTimeEnd,
                       @Param("endTimeBegin") LocalDateTime endTimeBegin,
                       @Param("endTimeEnd") LocalDateTime endTimeEnd,
                       @Param("locationType") String locationType);

    /**
     * 更新活动状态
     */
    @Update("UPDATE activities SET status = #{status} WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    /**
     * 增加参与人数
     */
    @Update("UPDATE activities SET current_participants = current_participants + 1 " +
            "WHERE id = #{id} AND current_participants < max_participants")
    int incrementParticipants(Long id);

    /**
     * 减少参与人数
     */
    @Update("UPDATE activities SET current_participants = current_participants - 1 " +
            "WHERE id = #{id} AND current_participants > 0")
    int decrementParticipants(Long id);

    /**
     * 检查活动是否已满
     */
    @Select("SELECT CASE WHEN max_participants = 0 THEN false " +
            "ELSE current_participants >= max_participants END " +
            "FROM activities WHERE id = #{id}")
    boolean isFull(Long id);

    /**
     * 增加浏览量
     */
    @Update("UPDATE activities SET views = views + 1 WHERE id = #{id}")
    int incrementViews(Long id);

    /**
     * 增加点赞数
     */
    @Update("UPDATE activities SET likes = likes + 1 WHERE id = #{id}")
    int incrementLikes(Long id);

    /**
     * 减少点赞数
     */
    @Update("UPDATE activities SET likes = likes - 1 WHERE id = #{id} AND likes > 0")
    int decrementLikes(Long id);

    /**
     * 获取用户参与的活动列表
     */
    @Select("SELECT a.* FROM activities a " +
            "INNER JOIN activity_participants ap ON a.id = ap.activity_id " +
            "WHERE ap.user_id = #{userId} AND ap.status = #{participantStatus} " +
            "ORDER BY a.created_at DESC " +
            "LIMIT #{offset}, #{limit}")
    List<Activity> getUserParticipatedActivities(@Param("userId") Long userId,
                                               @Param("participantStatus") Integer participantStatus,
                                               @Param("offset") Integer offset,
                                               @Param("limit") Integer limit);

    /**
     * 统计用户参与的活动数量
     */
    @Select("SELECT COUNT(*) FROM activities a " +
            "INNER JOIN activity_participants ap ON a.id = ap.activity_id " +
            "WHERE ap.user_id = #{userId} AND ap.status = #{participantStatus}")
    int countUserParticipatedActivities(@Param("userId") Long userId,
                                      @Param("participantStatus") Integer participantStatus);

    /**
     * 获取活动参与者列表
     */
    @Select("SELECT * FROM activity_participants " +
            "WHERE activity_id = #{activityId} AND status = #{status} " +
            "ORDER BY created_at " +
            "LIMIT #{offset}, #{limit}")
    List<ActivityParticipant> getActivityParticipants(@Param("activityId") Long activityId,
                                                     @Param("status") Integer status,
                                                     @Param("offset") Integer offset,
                                                     @Param("limit") Integer limit);

    /**
     * 获取活动参与者的详细信息（包含用户信息）
     */
    List<Map<String, Object>> getActivityParticipantsWithUser(@Param("activityId") Long activityId,
                                                             @Param("status") Integer status,
                                                             @Param("offset") Integer offset,
                                                             @Param("limit") Integer limit);

    /**
     * 统计活动参与者数量
     */
    @Select("SELECT COUNT(*) FROM activity_participants " +
            "WHERE activity_id = #{activityId} AND status = #{status}")
    int countActivityParticipants(@Param("activityId") Long activityId,
                                @Param("status") Integer status);

    /**
     * 添加活动参与者
     */
    @Insert("INSERT INTO activity_participants (activity_id, user_id, status) " +
            "VALUES (#{activityId}, #{userId}, #{status})")
    int insertParticipant(ActivityParticipant participant);

    /**
     * 批量添加活动参与者
     */
    int batchInsertParticipants(@Param("participants") List<ActivityParticipant> participants);

    /**
     * 更新参与者状态
     */
    @Update("UPDATE activity_participants SET status = #{status} " +
            "WHERE activity_id = #{activityId} AND user_id = #{userId}")
    int updateParticipantStatus(@Param("activityId") Long activityId,
                               @Param("userId") Long userId,
                               @Param("status") Integer status);

    /**
     * 删除活动参与者
     */
    @Delete("DELETE FROM activity_participants " +
            "WHERE activity_id = #{activityId} AND user_id = #{userId}")
    int deleteParticipant(@Param("activityId") Long activityId,
                         @Param("userId") Long userId);

    /**
     * 检查用户是否已参与活动
     */
    @Select("SELECT COUNT(*) FROM activity_participants " +
            "WHERE activity_id = #{activityId} AND user_id = #{userId}")
    int checkUserParticipation(@Param("activityId") Long activityId,
                              @Param("userId") Long userId);

    /**
     * 批量更新活动状态
     */
    int batchUpdateStatus(@Param("ids") List<Long> ids,
                         @Param("status") Integer status,
                         @Param("userId") Long userId);

    /**
     * 获取指定时间范围内的活动统计
     */
    Map<String, Object> getActivityStats(@Param("startTime") LocalDateTime startTime,
                                       @Param("endTime") LocalDateTime endTime,
                                       @Param("type") String type);

    /**
     * 获取热门活动
     */
    List<Activity> getHotActivities(@Param("type") String type,
                                  @Param("orderBy") String orderBy,
                                  @Param("limit") Integer limit);

    /**
     * 获取用户的活动统计信息
     */
    Map<String, Object> getUserActivityStats(@Param("userId") Long userId);

    /**
     * 统计用户的活动数
     * @param userId 用户ID
     * @return 活动数
     */
    @Select("SELECT COUNT(*) FROM activities WHERE user_id = #{userId}")
    int countByUser(@Param("userId") Long userId);

    /**
     * 统计用户参与的活动数
     */
    @Select("SELECT COUNT(*) FROM activities WHERE user_id = #{userId} AND status != -1")
    Integer countByUserId(@Param("userId") Long userId);

    /**
     * 获取用户最近的活动列表
     * @param userId 用户ID
     * @param limit 限制数量
     * @return 活动列表
     */
    @Select("SELECT a.id, a.title, a.type, a.created_at, " +
            "CASE a.type " +
            "   WHEN 'post' THEN CONCAT('发布了新帖子《', a.title, '》') " +
            "   WHEN 'reply' THEN CONCAT('回复了帖子《', a.title, '》') " +
            "   WHEN 'favorite' THEN CONCAT('收藏了帖子《', a.title, '》') " +
            "   ELSE CONCAT('参与了活动《', a.title, '》') " +
            "END as activity_text " +
            "FROM activities a " +
            "WHERE a.user_id = #{userId} " +
            "ORDER BY a.created_at DESC LIMIT #{limit}")
    List<Map<String, Object>> getRecentActivities(@Param("userId") Long userId, @Param("limit") int limit);
}
