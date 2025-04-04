package com.sinon.bluecommunity.user.service.serviceImpl;

import com.sinon.bluecommunity.common.entity.Activity;
import com.sinon.bluecommunity.common.entity.ActivityParticipant;
import com.sinon.bluecommunity.common.exception.BusinessException;
import com.sinon.bluecommunity.user.mapper.ActivityMapper;
import com.sinon.bluecommunity.user.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 活动服务实现类
 */
@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityMapper activityMapper;

    @Override
    @Transactional
    public Activity createActivity(Activity activity) {
        Assert.notNull(activity, "活动信息不能为空");
        Assert.notNull(activity.getUserId(), "发起人ID不能为空");
        Assert.hasText(activity.getTitle(), "活动标题不能为空");
        Assert.hasText(activity.getType(), "活动类型不能为空");
        Assert.hasText(activity.getDescription(), "活动描述不能为空");
        Assert.notNull(activity.getStartTime(), "开始时间不能为空");
        Assert.notNull(activity.getEndTime(), "结束时间不能为空");
        Assert.hasText(activity.getLocationType(), "活动地点类型不能为空");
        Assert.hasText(activity.getLocation(), "活动地点不能为空");

        if (activity.getStartTime().isAfter(activity.getEndTime())) {
            throw new BusinessException("活动开始时间不能晚于结束时间");
        }

        if (activity.getMaxParticipants() != null && activity.getMaxParticipants() < 0) {
            throw new BusinessException("最大参与人数不能小于0");
        }

        activity.setCurrentParticipants(0);
        activity.setViews(0);
        activity.setLikes(0);
        activity.setStatus(1); // 默认状态为正常

        int rows = activityMapper.insert(activity);
        if (rows != 1) {
            throw new BusinessException("创建活动失败");
        }

        return activity;
    }

    @Override
    @Transactional
    public Activity updateActivity(Activity activity) {
        Assert.notNull(activity, "活动信息不能为空");
        Assert.notNull(activity.getId(), "活动ID不能为空");
        Assert.notNull(activity.getUserId(), "发起人ID不能为空");

        Activity existing = activityMapper.selectById(activity.getId());
        if (existing == null) {
            throw new BusinessException("活动不存在");
        }

        if (!existing.getUserId().equals(activity.getUserId())) {
            throw new BusinessException("无权修改该活动");
        }

        if (existing.getStatus() != 1) {
            throw new BusinessException("只能修改正常状态的活动");
        }

        int rows = activityMapper.update(activity);
        if (rows != 1) {
            throw new BusinessException("更新活动失败");
        }

        return activityMapper.selectById(activity.getId());
    }

    @Override
    @Transactional
    public boolean deleteActivity(Long id, Long userId) {
        Assert.notNull(id, "活动ID不能为空");
        Assert.notNull(userId, "用户ID不能为空");

        Activity existing = activityMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException("活动不存在");
        }

        if (!existing.getUserId().equals(userId)) {
            throw new BusinessException("无权删除该活动");
        }

        int participantCount = activityMapper.countActivityParticipants(id, 1);
        if (participantCount > 0) {
            throw new BusinessException("该活动已有人报名，无法删除");
        }

        return activityMapper.deleteById(id, userId) == 1;
    }

    @Override
    public Activity getActivityById(Long id) {
        Assert.notNull(id, "活动ID不能为空");
        return activityMapper.selectById(id);
    }

    @Override
    public List<Activity> getActivityList(Long userId, Integer status, String type, String keyword,
                                        LocalDateTime startTimeBegin, LocalDateTime startTimeEnd,
                                        LocalDateTime endTimeBegin, LocalDateTime endTimeEnd,
                                        String locationType, String orderBy,
                                        Integer page, Integer size) {
        page = (page == null || page < 1) ? 1 : page;
        size = (size == null || size < 1) ? 10 : size;
        int offset = (page - 1) * size;

        return activityMapper.getActivityList(userId, status, type, keyword,
                startTimeBegin, startTimeEnd, endTimeBegin, endTimeEnd,
                locationType, orderBy, offset, size);
    }

    @Override
    public int countActivities(Long userId, Integer status, String type, String keyword,
                             LocalDateTime startTimeBegin, LocalDateTime startTimeEnd,
                             LocalDateTime endTimeBegin, LocalDateTime endTimeEnd,
                             String locationType) {
        return activityMapper.countActivities(userId, status, type, keyword,
                startTimeBegin, startTimeEnd, endTimeBegin, endTimeEnd, locationType);
    }

    @Override
    @Transactional
    public boolean updateStatus(Long id, Integer status) {
        Assert.notNull(id, "活动ID不能为空");
        Assert.notNull(status, "状态不能为空");

        Activity existing = activityMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException("活动不存在");
        }

        return activityMapper.updateStatus(id, status) == 1;
    }

    @Override
    @Transactional
    public boolean batchUpdateStatus(List<Long> ids, Integer status, Long userId) {
        Assert.notEmpty(ids, "活动ID列表不能为空");
        Assert.notNull(status, "状态不能为空");
        Assert.notNull(userId, "用户ID不能为空");

        return activityMapper.batchUpdateStatus(ids, status, userId) > 0;
    }

    @Override
    @Transactional
    public boolean joinActivity(Long activityId, Long userId) {
        Assert.notNull(activityId, "活动ID不能为空");
        Assert.notNull(userId, "用户ID不能为空");

        Activity activity = activityMapper.selectById(activityId);
        if (activity == null) {
            throw new BusinessException("活动不存在");
        }

        if (activity.getStatus() != 1) {
            throw new BusinessException("该活动不在进行中");
        }

        if (activity.getEndTime().isBefore(LocalDateTime.now())) {
            throw new BusinessException("该活动已结束");
        }

        if (activityMapper.checkUserParticipation(activityId, userId) > 0) {
            throw new BusinessException("您已经参与过该活动");
        }

        if (activityMapper.isFull(activityId)) {
            throw new BusinessException("该活动已满");
        }

        ActivityParticipant participant = new ActivityParticipant();
        participant.setActivityId(activityId);
        participant.setUserId(userId);
        participant.setStatus(1);

        if (activityMapper.insertParticipant(participant) != 1) {
            throw new BusinessException("参与活动失败");
        }

        if (activityMapper.incrementParticipants(activityId) != 1) {
            throw new BusinessException("更新参与人数失败");
        }

        return true;
    }

    @Override
    @Transactional
    public boolean cancelJoinActivity(Long activityId, Long userId) {
        Assert.notNull(activityId, "活动ID不能为空");
        Assert.notNull(userId, "用户ID不能为空");

        Activity activity = activityMapper.selectById(activityId);
        if (activity == null) {
            throw new BusinessException("活动不存在");
        }

        if (activityMapper.checkUserParticipation(activityId, userId) == 0) {
            throw new BusinessException("您未参与该活动");
        }

        if (activityMapper.deleteParticipant(activityId, userId) != 1) {
            throw new BusinessException("取消参与失败");
        }

        if (activityMapper.decrementParticipants(activityId) != 1) {
            throw new BusinessException("更新参与人数失败");
        }

        return true;
    }

    @Override
    public List<Map<String, Object>> getActivityParticipants(Long activityId, Integer status,
                                                            Integer page, Integer size) {
        Assert.notNull(activityId, "活动ID不能为空");
        
        page = (page == null || page < 1) ? 1 : page;
        size = (size == null || size < 1) ? 10 : size;
        int offset = (page - 1) * size;

        return activityMapper.getActivityParticipantsWithUser(activityId, status, offset, size);
    }

    @Override
    public int countActivityParticipants(Long activityId, Integer status) {
        Assert.notNull(activityId, "活动ID不能为空");
        return activityMapper.countActivityParticipants(activityId, status);
    }

    @Override
    @Transactional
    public boolean updateParticipantStatus(Long activityId, Long userId, Integer status) {
        Assert.notNull(activityId, "活动ID不能为空");
        Assert.notNull(userId, "用户ID不能为空");
        Assert.notNull(status, "状态不能为空");

        if (activityMapper.checkUserParticipation(activityId, userId) == 0) {
            throw new BusinessException("用户未参与该活动");
        }

        return activityMapper.updateParticipantStatus(activityId, userId, status) == 1;
    }

    @Override
    public List<Activity> getUserParticipatedActivities(Long userId, Integer status,
                                                      Integer page, Integer size) {
        Assert.notNull(userId, "用户ID不能为空");
        
        page = (page == null || page < 1) ? 1 : page;
        size = (size == null || size < 1) ? 10 : size;
        int offset = (page - 1) * size;

        return activityMapper.getUserParticipatedActivities(userId, status, offset, size);
    }

    @Override
    public int countUserParticipatedActivities(Long userId, Integer status) {
        Assert.notNull(userId, "用户ID不能为空");
        return activityMapper.countUserParticipatedActivities(userId, status);
    }

    @Override
    public List<Activity> getHotActivities(String type, String orderBy, Integer limit) {
        limit = (limit == null || limit < 1) ? 10 : limit;
        return activityMapper.getHotActivities(type, orderBy, limit);
    }

    @Override
    public Map<String, Object> getActivityStats(LocalDateTime startTime, LocalDateTime endTime, String type) {
        return activityMapper.getActivityStats(startTime, endTime, type);
    }

    @Override
    public Map<String, Object> getUserActivityStats(Long userId) {
        Assert.notNull(userId, "用户ID不能为空");
        return activityMapper.getUserActivityStats(userId);
    }

    @Override
    public void incrementViews(Long id) {
        Assert.notNull(id, "活动ID不能为空");
        activityMapper.incrementViews(id);
    }

    @Override
    @Transactional
    public boolean likeActivity(Long id) {
        Assert.notNull(id, "活动ID不能为空");

        Activity activity = activityMapper.selectById(id);
        if (activity == null) {
            throw new BusinessException("活动不存在");
        }

        return activityMapper.incrementLikes(id) == 1;
    }

    @Override
    @Transactional
    public boolean unlikeActivity(Long id) {
        Assert.notNull(id, "活动ID不能为空");

        Activity activity = activityMapper.selectById(id);
        if (activity == null) {
            throw new BusinessException("活动不存在");
        }

        return activityMapper.decrementLikes(id) == 1;
    }

    @Override
    public boolean hasJoined(Long activityId, Long userId) {
        Assert.notNull(activityId, "活动ID不能为空");
        Assert.notNull(userId, "用户ID不能为空");
        return activityMapper.checkUserParticipation(activityId, userId) > 0;
    }

    @Override
    public boolean isActivityFull(Long id) {
        Assert.notNull(id, "活动ID不能为空");
        return activityMapper.isFull(id);
    }
}
