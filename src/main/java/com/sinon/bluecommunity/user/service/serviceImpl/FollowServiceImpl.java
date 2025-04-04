package com.sinon.bluecommunity.user.service.serviceImpl;

import com.sinon.bluecommunity.common.entity.Follow;
import com.sinon.bluecommunity.common.exception.BusinessException;
import com.sinon.bluecommunity.common.vo.PageVO;
import com.sinon.bluecommunity.user.mapper.FollowMapper;
import com.sinon.bluecommunity.user.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 关注服务实现类
 */
@Service
public class FollowServiceImpl implements FollowService {

    @Autowired
    private FollowMapper followMapper;

    @Override
    @Transactional
    public void follow(Long followerId, Long followeeId) {
        // 参数校验
        if (followerId.equals(followeeId)) {
            throw new IllegalArgumentException("不能关注自己");
        }

        // 检查是否已关注
        if (hasFollowed(followerId, followeeId)) {
            throw new BusinessException("已经关注过该用户");
        }

        // 创建关注关系
        Follow follow = Follow.create(followerId, followeeId);
        followMapper.insert(follow);
    }

    @Override
    @Transactional
    public void unfollow(Long followerId, Long followeeId) {
        // 检查是否已关注
        if (!hasFollowed(followerId, followeeId)) {
            return;
        }

        // 删除关注关系
        followMapper.deleteByFollowerAndFollowee(followerId, followeeId);
    }

    @Override
    public boolean hasFollowed(Long followerId, Long followeeId) {
        Follow follow = followMapper.selectByFollowerAndFollowee(followerId, followeeId);
        return follow != null;
    }

    @Override
    public boolean isMutualFollow(Long userId1, Long userId2) {
        return hasFollowed(userId1, userId2) && hasFollowed(userId2, userId1);
    }

    @Override
    public PageVO<Follow> getFollowing(Long followerId, Integer page, Integer size) {
        // 计算偏移量
        int offset = (page - 1) * size;

        // 查询数据
        List<Follow> follows = followMapper.selectFollowing(followerId, offset, size);
        long total = followMapper.countFollowing(followerId);

        // 构建分页结果
        PageVO<Follow> pageVO = new PageVO<>(follows, total);
        pageVO.setHasMore(size, page);
        return pageVO;
    }

    @Override
    public PageVO<Follow> getFollowers(Long followeeId, Integer page, Integer size) {
        // 计算偏移量
        int offset = (page - 1) * size;

        // 查询数据
        List<Follow> follows = followMapper.selectFollowers(followeeId, offset, size);
        long total = followMapper.countFollowers(followeeId);

        // 构建分页结果
        PageVO<Follow> pageVO = new PageVO<>(follows, total);
        pageVO.setHasMore(size, page);
        return pageVO;
    }

    @Override
    public Long getFollowingCount(Long followerId) {
        return followMapper.countFollowing(followerId);
    }

    @Override
    public Long getFollowersCount(Long followeeId) {
        return followMapper.countFollowers(followeeId);
    }

    @Override
    public List<Follow> getFollowStatus(Long followerId, List<Long> followeeIds) {
        if (CollectionUtils.isEmpty(followeeIds)) {
            return new ArrayList<>();
        }

        // TODO: 需要在 FollowMapper 中添加批量查询方法
        // 临时实现：逐个查询
        List<Follow> result = new ArrayList<>();
        for (Long followeeId : followeeIds) {
            Follow follow = followMapper.selectByFollowerAndFollowee(followerId, followeeId);
            if (follow != null) {
                result.add(follow);
            }
        }
        return result;
    }
}
