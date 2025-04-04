package com.sinon.bluecommunity.user.service;

import com.sinon.bluecommunity.common.dto.UserProfileDTO;
import com.sinon.bluecommunity.common.entity.User;
import com.sinon.bluecommunity.common.vo.PageResult;

import java.util.List;
import java.util.Map;

/**
 * 用户服务接口
 */
public interface UserService {
    /**
     * 用户注册
     * @return 注册成功的用户信息
     */
    User register(String username, String password, String email);

    /**
     * 用户登录
     * @return 登录用户信息
     */
    User login(String username, String password);

    /**
     * 根据用户名查找用户
     * @return 用户信息，不存在返回null
     */
    User findByUsername(String username);

    /**
     * 根据邮箱查找用户
     * @return 用户信息，不存在返回null
     */
    User findByEmail(String email);

    /**
     * 根据ID查找用户
     * @return 用户信息，不存在返回null
     */
    User selectById(Long id);

    /**
     * 更新用户基本信息
     * @return 更新后的用户信息
     */
    User updateBasicInfo(User user);

    /**
     * 更新用户密码
     * @return 更新是否成功
     */
    boolean updatePassword(Long id, String oldPassword, String newPassword);

    /**
     * 更新用户头像
     * @return 更新后的用户信息
     */
    User updateAvatar(Long id, String avatarUrl);

    /**
     * 更新用户等级
     * @return 更新后的用户信息
     */
    User updateLevel(Long id, Integer level);

    /**
     * 批量更新用户等级
     * @return 更新的记录数
     */
    int batchUpdateLevel(List<Long> ids, Integer level);

    /**
     * 分页查询用户列表
     * @return 分页结果
     */
    PageResult<User> findPage(int page, int size, String username, String email, Integer level);

    /**
     * 分页查询用户列表（无条件）
     * @return 分页结果
     */
    PageResult<User> findPage(int page, int size);

    /**
     * 检查用户权限
     * @return 是否有权限
     */
    boolean checkPermission(Long userId, Long targetUserId);

    /**
     * 查询所有用户
     * @return 用户列表
     */
    List<User> findAll();

    /**
     * 获取用户的主页数据
     * @param userId 用户ID
     * @return 用户主页数据
     */
    UserProfileDTO getUserProfile(Long userId);

    /**
     * 获取当前用户的主页数据
     * @return 用户主页数据
     */
    UserProfileDTO getCurrentUserProfile();

    /**
     * 获取用户最近动态
     * @param userId 用户ID
     * @param limit 限制条数
     * @return 用户动态列表
     */
    List<Map<String, Object>> getUserRecentActions(Long userId, Integer limit);
}