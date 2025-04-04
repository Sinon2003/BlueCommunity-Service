package com.sinon.bluecommunity.user.service.serviceImpl;

import com.sinon.bluecommunity.common.entity.Like;
import com.sinon.bluecommunity.common.exception.BusinessException;
import com.sinon.bluecommunity.user.mapper.LikeMapper;
import com.sinon.bluecommunity.user.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 点赞服务实现类
 */
@Service
public class LikeServiceImpl implements LikeService {

    @Autowired
    private LikeMapper likeMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    // Redis key 前缀
    private static final String LIKE_COUNT_KEY = "like:count:";
    private static final String USER_LIKE_KEY = "like:user:";
    private static final long CACHE_EXPIRE_TIME = 24; // 缓存过期时间（小时）

    @Override
    @Transactional
    public void like(Long userId, Long targetId, String targetType) {
        // 验证目标类型
        Like like = new Like();
        like.setTargetType(targetType);
        if (!like.isValidTargetType()) {
            throw new IllegalArgumentException("无效的目标类型: " + targetType);
        }

        // 检查是否已点赞
        if (hasLiked(userId, targetId, targetType)) {
            throw new BusinessException("已经点赞过了");
        }

        // 添加点赞记录
        likeMapper.insert(userId, targetId, targetType);

        // 更新缓存
        String countKey = LIKE_COUNT_KEY + targetType + ":" + targetId;
        String userKey = USER_LIKE_KEY + userId + ":" + targetType + ":" + targetId;
        redisTemplate.opsForValue().increment(countKey);
        redisTemplate.opsForValue().set(userKey, "1", CACHE_EXPIRE_TIME, TimeUnit.HOURS);
    }

    @Override
    @Transactional
    public void unlike(Long userId, Long targetId, String targetType) {
        // 检查是否已点赞
        if (!hasLiked(userId, targetId, targetType)) {
            return; // 如果没有点赞，直接返回
        }

        // 删除点赞记录
        likeMapper.delete(userId, targetId, targetType);

        // 更新缓存
        String countKey = LIKE_COUNT_KEY + targetType + ":" + targetId;
        String userKey = USER_LIKE_KEY + userId + ":" + targetType + ":" + targetId;
        redisTemplate.opsForValue().decrement(countKey);
        redisTemplate.delete(userKey);
    }

    @Override
    public boolean hasLiked(Long userId, Long targetId, String targetType) {
        // 先查缓存
        String userKey = USER_LIKE_KEY + userId + ":" + targetType + ":" + targetId;
        String cached = redisTemplate.opsForValue().get(userKey);
        if (cached != null) {
            return "1".equals(cached);
        }

        // 缓存未命中，查数据库
        boolean hasLiked = likeMapper.exists(userId, targetId, targetType);
        
        // 写入缓存
        redisTemplate.opsForValue().set(userKey, hasLiked ? "1" : "0", CACHE_EXPIRE_TIME, TimeUnit.HOURS);
        
        return hasLiked;
    }

    @Override
    public int getLikeCount(Long targetId, String targetType) {
        // 先查缓存
        String countKey = LIKE_COUNT_KEY + targetType + ":" + targetId;
        String cached = redisTemplate.opsForValue().get(countKey);
        if (cached != null) {
            return Integer.parseInt(cached);
        }

        // 缓存未命中，查数据库
        int count = likeMapper.countByTarget(targetId, targetType);
        
        // 写入缓存
        redisTemplate.opsForValue().set(countKey, String.valueOf(count), CACHE_EXPIRE_TIME, TimeUnit.HOURS);
        
        return count;
    }

    @Override
    public List<Like> getUserLikes(Long userId, String targetType, Integer page, Integer size) {
        // 计算偏移量
        int offset = (page - 1) * size;
        
        // 这里需要在XML中实现分页查询
        // TODO: 实现分页查询
        return null;
    }

    @Override
    @Transactional
    public int deleteTargetLikes(Long targetId, String targetType) {
        // 删除数据库记录
        int count = likeMapper.deleteByTarget(targetId, targetType);
        
        // 删除缓存
        String countKey = LIKE_COUNT_KEY + targetType + ":" + targetId;
        redisTemplate.delete(countKey);
        
        // 注意：这里可能需要删除相关的用户点赞缓存，但是因为涉及多个用户，
        // 可以选择让这些缓存自然过期，或者通过其他方式批量删除
        
        return count;
    }

    @Override
    @Transactional
    public int deleteUserLikes(Long userId) {
        // 删除数据库记录
        int count = likeMapper.deleteByUser(userId);
        
        // 删除用户相关的所有缓存
        String userPattern = USER_LIKE_KEY + userId + ":*";
        redisTemplate.delete(redisTemplate.keys(userPattern));
        
        return count;
    }
}
