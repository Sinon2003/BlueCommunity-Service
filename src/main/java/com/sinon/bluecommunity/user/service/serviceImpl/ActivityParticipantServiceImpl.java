package com.sinon.bluecommunity.user.service.serviceImpl;

import com.sinon.bluecommunity.common.entity.ActivityParticipant;
import com.sinon.bluecommunity.common.exception.BusinessException;
import com.sinon.bluecommunity.user.mapper.ActivityParticipantMapper;
import com.sinon.bluecommunity.user.service.ActivityParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

/**
 * 活动参与者服务实现类
 */
@Service
public class ActivityParticipantServiceImpl implements ActivityParticipantService {

    @Autowired
    private ActivityParticipantMapper activityParticipantMapper;

    @Override
    public List<ActivityParticipant> listByActivityId(Long activityId) {
        Assert.notNull(activityId, "活动ID不能为空");
        return activityParticipantMapper.listByActivityId(activityId);
    }

    @Override
    public List<ActivityParticipant> listByActivityIdAndStatus(Long activityId, Integer status) {
        Assert.notNull(activityId, "活动ID不能为空");
        Assert.notNull(status, "状态不能为空");
        return activityParticipantMapper.listByActivityIdAndStatus(activityId, status);
    }

    @Override
    public List<ActivityParticipant> listByPage(Long activityId, Integer status, Integer page, Integer size) {
        Assert.notNull(activityId, "活动ID不能为空");
        
        page = (page == null || page < 1) ? 1 : page;
        size = (size == null || size < 1) ? 10 : size;
        int offset = (page - 1) * size;

        return activityParticipantMapper.listByPage(activityId, status, offset, size);
    }

    @Override
    public List<Map<String, Object>> listWithUserInfo(Long activityId, Integer status, Integer page, Integer size) {
        Assert.notNull(activityId, "活动ID不能为空");
        
        page = (page == null || page < 1) ? 1 : page;
        size = (size == null || size < 1) ? 10 : size;
        int offset = (page - 1) * size;

        return activityParticipantMapper.listWithUserInfo(activityId, status, offset, size);
    }

    @Override
    @Transactional
    public boolean updateStatus(Long id, Integer status) {
        Assert.notNull(id, "记录ID不能为空");
        Assert.notNull(status, "状态不能为空");

        return activityParticipantMapper.updateStatus(id, status) == 1;
    }

    @Override
    @Transactional
    public boolean updateStatusByActivityAndUser(Long activityId, Long userId, Integer status) {
        Assert.notNull(activityId, "活动ID不能为空");
        Assert.notNull(userId, "用户ID不能为空");
        Assert.notNull(status, "状态不能为空");

        ActivityParticipant participant = activityParticipantMapper.getByActivityIdAndUserId(activityId, userId);
        if (participant == null) {
            throw new BusinessException("参与记录不存在");
        }

        return activityParticipantMapper.updateStatusByActivityAndUser(activityId, userId, status) == 1;
    }

    @Override
    @Transactional
    public boolean batchUpdateStatus(List<Long> ids, Integer status) {
        Assert.notEmpty(ids, "记录ID列表不能为空");
        Assert.notNull(status, "状态不能为空");

        return activityParticipantMapper.batchUpdateStatus(ids, status) > 0;
    }

    @Override
    public ActivityParticipant getByActivityIdAndUserId(Long activityId, Long userId) {
        Assert.notNull(activityId, "活动ID不能为空");
        Assert.notNull(userId, "用户ID不能为空");

        return activityParticipantMapper.getByActivityIdAndUserId(activityId, userId);
    }

    @Override
    public int countParticipants(Long activityId, Integer status) {
        Assert.notNull(activityId, "活动ID不能为空");
        return activityParticipantMapper.countParticipants(activityId, status);
    }

    @Override
    @Transactional
    public boolean deleteByActivityIdAndUserId(Long activityId, Long userId) {
        Assert.notNull(activityId, "活动ID不能为空");
        Assert.notNull(userId, "用户ID不能为空");

        ActivityParticipant participant = activityParticipantMapper.getByActivityIdAndUserId(activityId, userId);
        if (participant == null) {
            throw new BusinessException("参与记录不存在");
        }

        return activityParticipantMapper.deleteByActivityIdAndUserId(activityId, userId) == 1;
    }

    @Override
    public List<Long> getParticipatedActivityIds(Long userId, Integer status) {
        Assert.notNull(userId, "用户ID不能为空");
        return activityParticipantMapper.getParticipatedActivityIds(userId, status);
    }

    @Override
    @Transactional
    public ActivityParticipant addParticipant(ActivityParticipant participant) {
        Assert.notNull(participant, "参与记录不能为空");
        Assert.notNull(participant.getActivityId(), "活动ID不能为空");
        Assert.notNull(participant.getUserId(), "用户ID不能为空");

        ActivityParticipant existing = activityParticipantMapper.getByActivityIdAndUserId(
                participant.getActivityId(), participant.getUserId());
        if (existing != null) {
            throw new BusinessException("用户已参与该活动");
        }

        if (activityParticipantMapper.insert(participant) != 1) {
            throw new BusinessException("添加参与记录失败");
        }

        return participant;
    }

    @Override
    @Transactional
    public boolean batchAddParticipants(List<ActivityParticipant> participants) {
        Assert.notEmpty(participants, "参与记录列表不能为空");

        for (ActivityParticipant participant : participants) {
            Assert.notNull(participant.getActivityId(), "活动ID不能为空");
            Assert.notNull(participant.getUserId(), "用户ID不能为空");

            ActivityParticipant existing = activityParticipantMapper.getByActivityIdAndUserId(
                    participant.getActivityId(), participant.getUserId());
            if (existing != null) {
                throw new BusinessException(String.format("用户 %d 已参与活动 %d",
                        participant.getUserId(), participant.getActivityId()));
            }
        }

        return activityParticipantMapper.batchInsert(participants) == participants.size();
    }

    @Override
    public List<Map<String, Object>> getUserParticipatedActivities(Long userId, Integer status, Integer page, Integer size) {
        Assert.notNull(userId, "用户ID不能为空");
        
        page = (page == null || page < 1) ? 1 : page;
        size = (size == null || size < 1) ? 10 : size;
        int offset = (page - 1) * size;

        return activityParticipantMapper.getUserParticipatedActivities(userId, status, offset, size);
    }

    @Override
    public int countUserParticipatedActivities(Long userId, Integer status) {
        Assert.notNull(userId, "用户ID不能为空");
        return activityParticipantMapper.countUserParticipatedActivities(userId, status);
    }
}
