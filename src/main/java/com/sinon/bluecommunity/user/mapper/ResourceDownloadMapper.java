package com.sinon.bluecommunity.user.mapper;

import com.sinon.bluecommunity.common.entity.ResourceDownload;
import org.apache.ibatis.annotations.*;

/**
 * 资源下载记录数据访问层接口
 */
@Mapper
public interface ResourceDownloadMapper {
    
    /**
     * 添加下载记录
     */
    @Insert("INSERT INTO resource_downloads (resource_id, user_id) VALUES (#{resourceId}, #{userId})")
    int insert(ResourceDownload download);

    /**
     * 检查是否已下载
     */
    @Select("SELECT COUNT(*) FROM resource_downloads WHERE resource_id = #{resourceId} AND user_id = #{userId}")
    boolean exists(@Param("resourceId") Long resourceId, @Param("userId") Long userId);

    /**
     * 统计下载次数
     */
    @Select("SELECT COUNT(*) FROM resource_downloads WHERE resource_id = #{resourceId}")
    int countByResourceId(Long resourceId);
}
