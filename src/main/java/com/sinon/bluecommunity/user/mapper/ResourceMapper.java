package com.sinon.bluecommunity.user.mapper;

import com.sinon.bluecommunity.common.dto.ResourceTypeCountDTO;
import com.sinon.bluecommunity.common.entity.Resource;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 资源数据访问层接口
 */
@Mapper
public interface ResourceMapper {
    
    /**
     * 获取资源列表（动态SQL在XML中实现）
     */
    List<Resource> getResourceList(@Param("userId") Long userId,
                                 @Param("type") String type,
                                 @Param("status") Integer status,
                                 @Param("keyword") String keyword,
                                 @Param("orderBy") String orderBy,
                                 @Param("offset") Integer offset,
                                 @Param("limit") Integer limit);

    /**
     * 统计资源数量（动态SQL在XML中实现）
     */
    int countResources(@Param("userId") Long userId,
                      @Param("type") String type,
                      @Param("status") Integer status,
                      @Param("keyword") String keyword);

    /**
     * 更新资源状态
     */
    @Update("UPDATE resources SET status = #{status} WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    /**
     * 增加下载次数
     */
    @Update("UPDATE resources SET downloads = downloads + 1 WHERE id = #{id}")
    int incrementDownloads(Long id);

    /**
     * 增加浏览次数
     */
    @Update("UPDATE resources SET views = views + 1 WHERE id = #{id}")
    int incrementViews(Long id);

    /**
     * 增加点赞数
     */
    @Update("UPDATE resources SET likes = likes + 1 WHERE id = #{id}")
    int incrementLikes(Long id);

    /**
     * 减少点赞数
     */
    @Update("UPDATE resources SET likes = likes - 1 WHERE id = #{id} AND likes > 0")
    int decrementLikes(Long id);

    /**
     * 获取热门资源
     */
    @Select("SELECT * FROM resources WHERE status = 1 ORDER BY downloads DESC, views DESC LIMIT #{limit}")
    List<Resource> getHotResources(Integer limit);

    /**
     * 根据类型获取资源列表
     */
    @Select("SELECT * FROM resources WHERE type = #{type} AND status = 1 ORDER BY created_at DESC LIMIT #{offset}, #{limit}")
    List<Resource> listByType(@Param("type") String type, @Param("offset") Integer offset, @Param("limit") Integer limit);

    /**
     * 统计各类型资源数量
     */
    @Select("SELECT type, COUNT(*) as count FROM resources WHERE status = 1 GROUP BY type")
    List<ResourceTypeCountDTO> countByType();

    /**
     * 获取用户上传的资源
     */
    @Select("SELECT * FROM resources WHERE user_id = #{userId} ORDER BY created_at DESC LIMIT #{offset}, #{limit}")
    List<Resource> getUserResources(@Param("userId") Long userId, @Param("offset") Integer offset, @Param("limit") Integer limit);

    /**
     * 统计用户上传的资源数量
     */
    @Select("SELECT COUNT(*) FROM resources WHERE user_id = #{userId}")
    int countUserResources(Long userId);

    /**
     * 根据ID查询资源
     */
    Resource selectById(Long id);

    /**
     * 插入资源
     */
    int insert(Resource resource);

    /**
     * 更新资源
     */
    int update(Resource resource);

    /**
     * 删除资源
     */
    int delete(@Param("id") Long id, @Param("userId") Long userId);

    /**
     * 批量更新资源状态
     */
    int batchUpdateStatus(@Param("ids") List<Long> ids, @Param("status") Integer status);
}
