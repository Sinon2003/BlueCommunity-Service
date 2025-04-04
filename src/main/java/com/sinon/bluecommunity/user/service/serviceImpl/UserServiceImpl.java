package com.sinon.bluecommunity.user.service.serviceImpl;

import com.sinon.bluecommunity.common.dto.UserProfileDTO;
import com.sinon.bluecommunity.common.entity.User;
import com.sinon.bluecommunity.common.exception.BusinessException;
import com.sinon.bluecommunity.common.utils.ThreadLocalUtil;
import com.sinon.bluecommunity.common.vo.PageResult;
import com.sinon.bluecommunity.common.enums.ResultCode;
import com.sinon.bluecommunity.user.mapper.ActivityMapper;
import com.sinon.bluecommunity.user.mapper.CommentMapper;
import com.sinon.bluecommunity.user.mapper.LikeMapper;
import com.sinon.bluecommunity.user.mapper.TopicMapper;
import com.sinon.bluecommunity.user.mapper.UserMapper;
import com.sinon.bluecommunity.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * 用户服务实现类
 */
@Slf4j
@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private LikeMapper likeMapper;

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private TopicMapper topicMapper;

    @Override
    public User register(String username, String password, String email) {
        log.info("开始注册用户: {}", username);
        
        // 检查用户名是否已存在
        if (userMapper.checkUsernameExists(username) > 0) {
            throw new BusinessException(ResultCode.USERNAME_EXISTS);
        }

        // 检查邮箱是否已存在
        if (StringUtils.hasText(email) && userMapper.checkEmailExists(email) > 0) {
            throw new BusinessException(ResultCode.EMAIL_EXISTS);
        }

        // 创建新用户
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setLevel(1); // 默认等级为1

        userMapper.insert(user);
        log.info("用户注册成功: {}", username);
        return user;
    }

    @Override
    public User login(String username, String password) {
        log.debug("尝试登录: {}", username);
        User user = userMapper.selectByUsername(username);
        
        // 用户不存在、密码错误都返回相同的错误信息
        if (user == null) {
            throw new BusinessException("用户名或密码错误");
        }

        // 校验密码
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }

        // 检查用户状态
        if (user.isDisabled()) {
            throw new BusinessException("账号已被禁用");
        }
        if (user.isLocked()) {
            throw new BusinessException("账号已被锁定");
        }

        // 更新最后登录时间
        userMapper.updateLastLoginTime(user.getId());
        
        log.info("登录成功: {}", username);
        return user;
    }

    @Override
    public User findByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    @Override
    public User findByEmail(String email) {
        return userMapper.selectByEmail(email);
    }

    @Override
    public User updateBasicInfo(User user) {
        log.info("更新用户基本信息: {}", user.getId());
        User existingUser = userMapper.selectById(user.getId());
        if (existingUser == null) {
            log.warn("更新失败: 数据不存在 - {}", user.getId());
            throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        }

        // 如果要更新邮箱，检查新邮箱是否已被使用
        if (StringUtils.hasText(user.getEmail()) && 
            !user.getEmail().equals(existingUser.getEmail()) && 
            userMapper.checkEmailExists(user.getEmail()) > 0) {
            throw new BusinessException(ResultCode.EMAIL_EXISTS);

        }

        userMapper.updateBasicInfo(user);
        return userMapper.selectById(user.getId());
    }

    @Override
    public boolean updatePassword(Long id, String oldPassword, String newPassword) {
        log.info("更新用户密码: {}", id);
        User user = userMapper.selectById(id);
        if (user == null) {
            log.warn("更新密码失败: 数据不存在 - {}", id);
            throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        }

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            log.debug("更新密码失败: 原密码错误");
            return false;
        }

        userMapper.updatePassword(id, passwordEncoder.encode(newPassword));
        log.info("密码更新成功: {}", id);
        return true;
    }

    @Override
    public User updateAvatar(Long id, String avatarUrl) {
        log.info("更新用户头像: {}", id);
        User user = userMapper.selectById(id);
        if (user == null) {
            log.warn("更新头像失败: 数据不存在 - {}", id);
            throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        }

        userMapper.updateAvatar(id, avatarUrl);
        return userMapper.selectById(id);
    }

    @Override
    public User updateLevel(Long id, Integer level) {
        log.info("更新用户等级: {} -> {}", id, level);
        User user = userMapper.selectById(id);
        if (user == null) {
            log.warn("更新等级失败: 数据不存在 - {}", id);
            throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        }

        userMapper.updateLevel(id, level);
        return userMapper.selectById(id);
    }

    @Override
    public int batchUpdateLevel(List<Long> ids, Integer level) {
        log.info("批量更新用户等级: {} -> {}", ids, level);
        return userMapper.batchUpdateLevel(ids, level);
    }

    @Override
    public PageResult<User> findPage(int page, int size, String username, String email, Integer level) {
        int offset = (page - 1) * size;
        List<User> users = userMapper.selectByCondition(level, email, username, offset, size);
        long total = userMapper.count(level, email, username);
        return new PageResult<>(users, page, size, total);
    }

    @Override
    public PageResult<User> findPage(int page, int size) {
        // 调用重载的findPage方法，不指定任何条件
        return findPage(page, size, null, null, null);
    }

    @Override
    public boolean checkPermission(Long userId, Long targetUserId) {
        User user = userMapper.selectById(userId);
        User targetUser = userMapper.selectById(targetUserId);
        
        if (user == null || targetUser == null) {
            return false;
        }
        
        // 用户只能操作自己的数据
        return userId.equals(targetUserId);
    }

    @Override
    public List<User> findAll() {
        return userMapper.selectAll();
    }

    @Override
    public User selectById(Long id) {
        return userMapper.selectById(id);
    }

    @Override
    public UserProfileDTO getUserProfile(Long userId) {
        UserProfileDTO profile = new UserProfileDTO();
        
        // 获取用户话题数
        Integer topicCount = topicMapper.countByUserId(userId);
        profile.setTopicCount(topicCount);
        
        // 获取用户评论数
        Integer commentCount = commentMapper.countByUserId(userId);
        profile.setCommentCount(commentCount);
        
        // 获取用户点赞数
        Integer likeCount = likeMapper.countByUserId(userId);
        profile.setLikeCount(likeCount);
        
        // 获取用户活动数
        Integer activityCount = activityMapper.countByUserId(userId);
        profile.setActivityCount(activityCount);
        
        // 暂时不获取最近活动、最近发帖和最近评论，可以后续添加
        
        return profile;
    }

    @Override
    public UserProfileDTO getCurrentUserProfile() {
        // 从ThreadLocal获取当前用户ID
        Map<String, Object> map = ThreadLocalUtil.get();
        Long userId = Long.valueOf(map.get("userId").toString());

        UserProfileDTO profileDTO = new UserProfileDTO();

        // 获取评论数
        int commentCount = commentMapper.countByUser(userId);
        profileDTO.setCommentCount(commentCount);

        // 获取点赞数（收到的点赞）
        int likeCount = likeMapper.countByUser(userId, null);
        profileDTO.setLikeCount(likeCount);

        // 获取活动数
        int activityCount = activityMapper.countByUser(userId);
        profileDTO.setActivityCount(activityCount);

        // 获取最近的活动列表（默认3条）
        List<Map<String, Object>> recentActivities = activityMapper.getRecentActivities(userId, 3);
        profileDTO.setRecentActivities(recentActivities);

        // 获取最近的评论列表（默认3条）
        List<Map<String, Object>> recentComments = commentMapper.getRecentComments(userId, 3);
        profileDTO.setRecentComments(recentComments);

        return profileDTO;
    }

    @Override
    public List<Map<String, Object>> getUserRecentActions(Long userId, Integer limit) {
        Assert.notNull(userId, "用户ID不能为空");
        limit = (limit == null || limit < 1) ? 3 : limit;  // 默认返回3条
        return userMapper.getUserRecentActions(userId, limit);
    }
}