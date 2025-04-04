package com.sinon.bluecommunity.user.mapper;

import com.sinon.bluecommunity.common.entity.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * 评论数据访问层接口
 */
@Mapper
public interface CommentMapper {
    
    /**
     * 根据ID获取评论（包含用户信息）
     */
    @Select("SELECT c.*, u.username, u.nickname, u.avatar_url, " +
            "ru.username as reply_username, ru.nickname as reply_nickname " +
            "FROM comment c " +
            "LEFT JOIN user u ON c.user_id = u.id " +
            "LEFT JOIN user ru ON c.reply_user_id = ru.id " +
            "WHERE c.id = #{id}")
    Comment getById(Long id);

    /**
     * 插入评论
     */
    @Insert("INSERT INTO comment (target_id, target_type, parent_id, user_id, reply_user_id, level, content, created_at, updated_at) " +
            "VALUES (#{targetId}, #{targetType}, #{parentId}, #{userId}, #{replyUserId}, #{level}, #{content}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Comment comment);

    /**
     * 更新评论
     */
    @Update("UPDATE comment SET content = #{content}, updated_at = NOW() WHERE id = #{id}")
    int update(Comment comment);

    /**
     * 删除评论
     */
    @Delete("DELETE FROM comment WHERE id = #{id}")
    int deleteById(Long id);

    /**
     * 根据目标获取评论列表（动态SQL在XML中实现）
     * @param targetId 目标ID
     * @param targetType 目标类型
     * @param userId 用户ID
     * @param parentId 父评论ID
     * @param level 评论层级
     * @param orderBy 排序方式
     * @param page 页码
     * @param size 每页大小
     * @param offset 偏移量
     * @return 评论列表
     */
    List<Comment> getCommentList(@Param("targetId") Long targetId,
                               @Param("targetType") String targetType,
                               @Param("userId") Long userId,
                               @Param("parentId") Long parentId,
                               @Param("level") Integer level,
                               @Param("orderBy") String orderBy,
                               @Param("page") Integer page,
                               @Param("size") Integer size,
                               @Param("offset") Integer offset);

    /**
     * 统计评论数量（动态SQL在XML中实现）
     */
    Long countComments(@Param("targetId") Long targetId,
                     @Param("targetType") String targetType,
                     @Param("userId") Long userId);

    /**
     * 获取用户的评论数量
     */
    @Select("SELECT COUNT(*) FROM comment WHERE user_id = #{userId}")
    Integer countByUserId(Long userId);

    /**
     * 获取目标的评论数量
     */
    @Select("SELECT COUNT(*) FROM comment WHERE target_id = #{targetId} AND target_type = #{targetType}")
    Long countByTarget(@Param("targetId") Long targetId, @Param("targetType") String targetType);

    /**
     * 删除目标的所有评论
     */
    @Delete("DELETE FROM comment WHERE target_id = #{targetId} AND target_type = #{targetType}")
    int deleteByTarget(@Param("targetId") Long targetId, @Param("targetType") String targetType);

    /**
     * 批量删除评论（动态SQL在XML中实现）
     */
    int batchDelete(@Param("ids") List<Long> ids);

    /**
     * 检查评论是否存在
     */
    @Select("SELECT COUNT(*) > 0 FROM comment WHERE id = #{id}")
    boolean exists(Long id);

    /**
     * 检查用户是否是评论的作者
     */
    @Select("SELECT COUNT(*) > 0 FROM comment WHERE id = #{commentId} AND user_id = #{userId}")
    boolean isCommentAuthor(@Param("commentId") Long commentId, @Param("userId") Long userId);

    /**
     * 获取最新的N条评论
     */
    @Select("SELECT c.*, u.username, u.nickname, u.avatar_url " +
            "FROM comment c " +
            "LEFT JOIN user u ON c.user_id = u.id " +
            "ORDER BY c.created_at DESC " +
            "LIMIT #{limit}")
    List<Comment> getLatestComments(@Param("limit") Integer limit);

    /**
     * 删除用户的所有评论
     */
    @Delete("DELETE FROM comment WHERE user_id = #{userId}")
    int deleteByUserId(Long userId);

    /**
     * 统计用户的评论数
     * @param userId 用户ID
     * @return 评论数
     */
    int countByUser(@Param("userId") Long userId);

//    /**
//     * 统计用户的评论数
//     */
//    @Select("SELECT COUNT(*) FROM comments WHERE user_id = #{userId} AND status != -1")
//    Integer countByUserId(@Param("userId") Long userId);

    /**
     * 获取用户最近的评论列表
     * @param userId 用户ID
     * @param limit 限制数量
     * @return 评论列表
     */
    List<Map<String, Object>> getRecentComments(@Param("userId") Long userId, @Param("limit") int limit);
}
