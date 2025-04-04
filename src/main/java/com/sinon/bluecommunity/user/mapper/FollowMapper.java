package com.sinon.bluecommunity.user.mapper;

import com.sinon.bluecommunity.common.entity.Follow;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 关注关系Mapper接口
 */
@Mapper
public interface FollowMapper {
    
    /**
     * 查询是否已关注
     * @param followerId 关注者ID
     * @param followeeId 被关注者ID
     * @return 关注记录
     */
    @Select("SELECT * FROM follow " +
            "WHERE follower_id = #{followerId} " +
            "AND followee_id = #{followeeId}")
    Follow selectByFollowerAndFollowee(@Param("followerId") Long followerId,
                                     @Param("followeeId") Long followeeId);

    /**
     * 获取用户的关注列表
     * @param followerId 关注者ID
     * @param offset 偏移量
     * @param limit 限制数量
     * @return 关注列表
     */
    @Select("SELECT f.*, u.username, u.nickname, u.avatar_url, u.bio " +
            "FROM follow f " +
            "LEFT JOIN user u ON f.followee_id = u.id " +
            "WHERE f.follower_id = #{followerId} " +
            "ORDER BY f.created_at DESC " +
            "LIMIT #{offset}, #{limit}")
    List<Follow> selectFollowing(@Param("followerId") Long followerId,
                               @Param("offset") int offset,
                               @Param("limit") int limit);

    /**
     * 获取用户的粉丝列表
     * @param followeeId 被关注者ID
     * @param offset 偏移量
     * @param limit 限制数量
     * @return 粉丝列表
     */
    @Select("SELECT f.*, u.username, u.nickname, u.avatar_url, u.bio " +
            "FROM follow f " +
            "LEFT JOIN user u ON f.follower_id = u.id " +
            "WHERE f.followee_id = #{followeeId} " +
            "ORDER BY f.created_at DESC " +
            "LIMIT #{offset}, #{limit}")
    List<Follow> selectFollowers(@Param("followeeId") Long followeeId,
                               @Param("offset") int offset,
                               @Param("limit") int limit);

    /**
     * 统计用户的关注数
     * @param followerId 关注者ID
     * @return 关注数
     */
    @Select("SELECT COUNT(*) FROM follow WHERE follower_id = #{followerId}")
    long countFollowing(@Param("followerId") Long followerId);

    /**
     * 统计用户的粉丝数
     * @param followeeId 被关注者ID
     * @return 粉丝数
     */
    @Select("SELECT COUNT(*) FROM follow WHERE followee_id = #{followeeId}")
    long countFollowers(@Param("followeeId") Long followeeId);

    /**
     * 添加关注关系
     * @param follow 关注对象
     * @return 影响行数
     */
    @Insert("INSERT INTO follow (" +
            "follower_id, followee_id, created_at" +
            ") VALUES (" +
            "#{followerId}, #{followeeId}, NOW()" +
            ")")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Follow follow);

    /**
     * 取消关注
     * @param followerId 关注者ID
     * @param followeeId 被关注者ID
     * @return 影响行数
     */
    @Delete("DELETE FROM follow " +
            "WHERE follower_id = #{followerId} " +
            "AND followee_id = #{followeeId}")
    int deleteByFollowerAndFollowee(@Param("followerId") Long followerId,
                                  @Param("followeeId") Long followeeId);

    /**
     * 获取共同关注列表
     * @param userIdA 用户A的ID
     * @param userIdB 用户B的ID
     * @param offset 偏移量
     * @param limit 限制数量
     * @return 共同关注的用户列表
     */
    @Select("SELECT f1.*, u.username, u.nickname, u.avatar_url, u.bio " +
            "FROM follow f1 " +
            "JOIN follow f2 ON f1.followee_id = f2.followee_id " +
            "LEFT JOIN user u ON f1.followee_id = u.id " +
            "WHERE f1.follower_id = #{userIdA} " +
            "AND f2.follower_id = #{userIdB} " +
            "ORDER BY f1.created_at DESC " +
            "LIMIT #{offset}, #{limit}")
    List<Follow> selectCommonFollowing(@Param("userIdA") Long userIdA,
                                     @Param("userIdB") Long userIdB,
                                     @Param("offset") int offset,
                                     @Param("limit") int limit);
}
