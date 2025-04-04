package com.sinon.bluecommunity.user.mapper;

import com.sinon.bluecommunity.common.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * 用户Mapper接口
 */
@Mapper
public interface UserMapper {

    /**
     * 根据用户名查询用户
     */
    @Select("SELECT id, username, nickname, password, level, email, bio, avatar_url, created_at, updated_at, status, last_login_at " +
            "FROM user WHERE username = #{username}")
    User selectByUsername(String username);

    /**
     * 根据邮箱查询用户
     */
    @Select("SELECT id, username, nickname, password, level, email, bio, avatar_url, created_at, updated_at, status, last_login_at " +
            "FROM user WHERE email = #{email}")
    User selectByEmail(String email);

    /**
     * 根据ID查询用户
     */
    @Select("SELECT id, username, nickname, password, level, email, bio, avatar_url, created_at, updated_at, status, last_login_at " +
            "FROM user WHERE id = #{id}")
    User selectById(Long id);

    /**
     * 插入新用户
     */
    @Insert("INSERT INTO user (username, password, nickname, email, level, bio, avatar_url) " +
            "VALUES (#{username}, #{password}, #{nickname}, #{email}, #{level}, #{bio}, #{avatarUrl})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(User user);

    /**
     * 更新用户基本信息（动态SQL在XML中）
     */
    void updateBasicInfo(User user);

    /**
     * 更新用户密码
     */
    @Update("UPDATE user SET password = #{newPassword} WHERE id = #{id}")
    void updatePassword(@Param("id") Long id, @Param("newPassword") String newPassword);

    /**
     * 更新用户头像
     */
    @Update("UPDATE user SET avatar_url = #{avatarUrl} WHERE id = #{id}")
    void updateAvatar(@Param("id") Long id, @Param("avatarUrl") String avatarUrl);

    /**
     * 更新用户等级
     */
    @Update("UPDATE user SET level = #{level} WHERE id = #{id}")
    void updateLevel(@Param("id") Long id, @Param("level") Integer level);

    /**
     * 批量更新用户等级
     *
     * @param ids 用户ID列表
     * @param level 等级
     * @return 影响行数
     */
    int batchUpdateLevel(@Param("ids") List<Long> ids, @Param("level") Integer level);

    /**
     * 根据条件查询用户列表（动态SQL在XML中）
     */
    List<User> selectByCondition(
            @Param("level") Integer level,
            @Param("email") String email,
            @Param("username") String username,
            @Param("offset") int offset,
            @Param("size") int size);

    /**
     * 统计用户总数（动态SQL在XML中）
     */
    long count(
            @Param("level") Integer level,
            @Param("email") String email,
            @Param("username") String username);

    /**
     * 查询所有用户
     */
    @Select("SELECT id, username, nickname, password, level, email, bio, avatar_url, created_at, updated_at, status, last_login_at " +
            "FROM user ORDER BY created_at DESC")
    List<User> selectAll();

    /**
     * 删除用户
     */
    @Delete("DELETE FROM user WHERE id = #{id}")
    void deleteById(Long id);

    /**
     * 检查用户名是否存在
     */
    @Select("SELECT COUNT(*) FROM user WHERE username = #{username}")
    int checkUsernameExists(String username);

    /**
     * 检查邮箱是否存在
     */
    @Select("SELECT COUNT(*) FROM user WHERE email = #{email}")
    int checkEmailExists(String email);

    /**
     * 批量查询用户信息
     * @param ids 用户ID列表
     * @return 用户列表
     */
    List<User> selectByIds(@Param("ids") List<Long> ids);

    /**
     * 更新用户最后登录时间
     */
    @Update("UPDATE user SET last_login_at = CURRENT_TIMESTAMP WHERE id = #{id}")
    int updateLastLoginTime(@Param("id") Long id);

    /**
     * 获取用户最近动态
     * @param userId 用户ID
     * @param limit 限制条数
     * @return 用户动态列表
     */
    List<Map<String, Object>> getUserRecentActions(@Param("userId") Long userId, @Param("limit") Integer limit);
}