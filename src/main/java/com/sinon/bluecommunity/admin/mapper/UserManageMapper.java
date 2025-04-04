package com.sinon.bluecommunity.admin.mapper;

import com.sinon.bluecommunity.common.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 用户管理Mapper接口
 */
@Mapper
public interface UserManageMapper {

    /**
     * 获取所有用户
     */
    @Select("SELECT * FROM user ORDER BY created_at DESC")
    List<User> getAllUsers();

    /**
     * 根据ID获取用户
     */
    @Select("SELECT * FROM user WHERE id = #{id}")
    User getUserById(Long id);

    /**
     * 更新用户状态
     */
//    @Update("UPDATE user SET status = #{status}, updated_at = NOW() WHERE id = #{id}")
//    int updateUserStatus(@Param("id") Long id, @Param("status") Integer status);

    /**
     * 删除用户
     */
    @Delete("DELETE FROM user WHERE id = #{id}")
    int deleteUser(Long id);

    /**
     * 批量删除用户
     */
    int batchDeleteUsers(@Param("ids") List<Long> ids);

    /**
     * 搜索用户
     */
    List<User> searchUsers(@Param("keyword") String keyword,
                          @Param("status") Integer status,
                          @Param("offset") Integer offset,
                          @Param("limit") Integer limit);

    /**
     * 统计用户数量
     */
    @Select("SELECT COUNT(*) FROM user")
    int countUsers();
}
