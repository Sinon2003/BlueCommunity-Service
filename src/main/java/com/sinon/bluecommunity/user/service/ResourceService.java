package com.sinon.bluecommunity.user.service;

import com.sinon.bluecommunity.common.dto.ResourceCreateDTO;
import com.sinon.bluecommunity.common.dto.ResourceTypeCountDTO;
import com.sinon.bluecommunity.common.entity.Resource;
import com.sinon.bluecommunity.common.vo.PageVO;

import java.util.List;

/**
 * 资源服务接口
 */
public interface ResourceService {
    
    /**
     * 创建资源
     */
    Resource createResource(ResourceCreateDTO dto, Long userId);

    /**
     * 更新资源
     */
    Resource updateResource(Resource resource);

    /**
     * 删除资源
     */
    boolean deleteResource(Long id, Long userId);

    /**
     * 获取资源详情
     */
    Resource getResourceById(Long id, Long currentUserId);

    /**
     * 分页获取资源列表
     */
    PageVO<Resource> getResourceList(Long userId, String type, Integer status, String keyword, 
                                   String orderBy, Integer page, Integer size, Long currentUserId);

    /**
     * 获取热门资源
     */
    List<Resource> getHotResources(Integer limit, Long currentUserId);

    /**
     * 按类型获取资源列表
     */
    List<Resource> getResourcesByType(String type, Integer page, Integer size, Long currentUserId);

    /**
     * 获取用户上传的资源
     */
    List<Resource> getUserResources(Long userId, Integer page, Integer size, Long currentUserId);

    /**
     * 统计各类型资源数量
     */
    List<ResourceTypeCountDTO> countResourcesByType();

    /**
     * 点赞资源
     */
    boolean likeResource(Long id, Long userId);

    /**
     * 取消点赞
     */
    boolean unlikeResource(Long id, Long userId);

    /**
     * 下载资源
     */
    boolean downloadResource(Long id, Long userId);

    /**
     * 增加浏览次数
     */
    boolean incrementViews(Long id);

    /**
     * 批量更新资源状态
     */
    boolean batchUpdateStatus(List<Long> ids, Integer status);

    /**
     * 审核资源
     */
    boolean reviewResource(Long id, Integer status, String reason);
}
