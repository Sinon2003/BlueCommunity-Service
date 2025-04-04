package com.sinon.bluecommunity.user.service.serviceImpl;

import com.sinon.bluecommunity.common.dto.ResourceCreateDTO;
import com.sinon.bluecommunity.common.dto.ResourceTypeCountDTO;
import com.sinon.bluecommunity.common.entity.Resource;
import com.sinon.bluecommunity.common.entity.ResourceDownload;
import com.sinon.bluecommunity.common.entity.ResourceLike;
import com.sinon.bluecommunity.common.entity.ResourceReview;
import com.sinon.bluecommunity.common.entity.Tag;
import com.sinon.bluecommunity.common.exception.BusinessException;
import com.sinon.bluecommunity.common.vo.PageVO;
import com.sinon.bluecommunity.user.mapper.ResourceDownloadMapper;
import com.sinon.bluecommunity.user.mapper.ResourceLikeMapper;
import com.sinon.bluecommunity.user.mapper.ResourceMapper;
import com.sinon.bluecommunity.user.mapper.ResourceReviewMapper;
import com.sinon.bluecommunity.user.mapper.TagMapper;
import com.sinon.bluecommunity.user.mapper.TagRelationMapper;
import com.sinon.bluecommunity.user.service.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sinon.bluecommunity.common.utils.ThreadLocalUtil;

import java.util.List;

/**
 * 资源服务实现类
 */
@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {

    private final ResourceMapper resourceMapper;
    private final ResourceLikeMapper resourceLikeMapper;
    private final ResourceDownloadMapper resourceDownloadMapper;
    private final ResourceReviewMapper resourceReviewMapper;
    private final TagMapper tagMapper;
    private final TagRelationMapper tagRelationMapper;

    @Override
    @Transactional
    public Resource createResource(ResourceCreateDTO dto, Long userId) {
        // 创建资源对象
        Resource resource = new Resource();
        resource.setUserId(userId);
        resource.setTitle(dto.getTitle());
        resource.setType(dto.getType());
        resource.setUrlType(dto.getUrlType());
        resource.setResourceUrl(dto.getResourceUrl());
        resource.setDescription(dto.getDescription());
        resource.setContent(dto.getContent());
        resource.setCoverUrl(dto.getCoverUrl());
        resource.setStatus(Resource.STATUS_PENDING);
        resource.initializeCounters();

        // 验证资源信息
        if (!resource.isValidAll()) {
            throw new BusinessException("资源信息不完整或格式不正确");
        }

        // 插入资源
        resourceMapper.insert(resource);

        // 处理标签
        if (dto.getTags() != null && !dto.getTags().isEmpty()) {
            for (String tagName : dto.getTags()) {
                // 获取或创建标签
                Tag tag = tagMapper.selectByName(tagName);
                if (tag == null) {
                    tag = new Tag();
                    tag.setName(tagName);
                    tag.setStatus(1);
                    tag.setUsageCount(0);
                    tagMapper.insert(tag);
                }
                
                // 创建标签关联
                tagRelationMapper.insertSimple(tag.getId(), resource.getId(), "resource");
                
                // 更新标签使用次数
                tagMapper.incrementUsageCount(tag.getId());
            }
        }

        return resource;
    }

    @Override
    @Transactional
    public Resource updateResource(Resource resource) {
        // 验证资源
        if (!resource.isValidAll()) {
            throw new BusinessException("资源信息不完整");
        }

        // 检查资源是否存在
        Resource existingResource = resourceMapper.selectById(resource.getId());
        if (existingResource == null) {
            throw new BusinessException("资源不存在");
        }

        // 检查是否有权限修改
        if (!existingResource.getUserId().equals(resource.getUserId())) {
            throw new BusinessException("无权修改该资源");
        }

        // 更新资源
        resourceMapper.update(resource);
        return resourceMapper.selectById(resource.getId());
    }

    @Override
    @Transactional
    public boolean deleteResource(Long id, Long userId) {
        return resourceMapper.delete(id, userId) > 0;
    }

    @Override
    public Resource getResourceById(Long id, Long currentUserId) {
        Resource resource = resourceMapper.selectById(id);
        if (resource != null) {
            // 增加浏览次数
            incrementViews(id);
            // 设置当前用户是否已点赞/下载
            setUserOperationStatus(resource, currentUserId);
        }
        return resource;
    }

    @Override
    public PageVO<Resource> getResourceList(Long userId, String type, Integer status, String keyword,
                                          String orderBy, Integer page, Integer size, Long currentUserId) {
        // 计算偏移量
        Integer offset = (page - 1) * size;

        // 转换排序字段
        String dbOrderBy = null;
        if (orderBy != null) {
            switch (orderBy.toLowerCase()) {
                case "newest":
                    dbOrderBy = "r.created_at DESC";
                    break;
                case "oldest":
                    dbOrderBy = "r.created_at ASC";
                    break;
                case "most_liked":
                    dbOrderBy = "r.likes DESC";
                    break;
                case "most_downloaded":
                    dbOrderBy = "r.downloads DESC";
                    break;
                case "most_viewed":
                    dbOrderBy = "r.views DESC";
                    break;
            }
        }

        // 查询资源列表
        List<Resource> resources = resourceMapper.getResourceList(userId, type, status, keyword, dbOrderBy, offset, size);
        
        // 设置用户操作状态
        resources.forEach(resource -> setUserOperationStatus(resource, currentUserId));

        // 查询总数
        int total = resourceMapper.countResources(userId, type, status, keyword);

        // 创建分页对象
        PageVO<Resource> pageVO = new PageVO<>(resources, (long) total);
        pageVO.setHasMore(size, page);
        
        return pageVO;
    }

    @Override
    public List<Resource> getHotResources(Integer limit, Long currentUserId) {
        List<Resource> resources = resourceMapper.getHotResources(limit);
        resources.forEach(resource -> setUserOperationStatus(resource, currentUserId));
        return resources;
    }

    @Override
    public List<Resource> getResourcesByType(String type, Integer page, Integer size, Long currentUserId) {
        Integer offset = (page - 1) * size;
        List<Resource> resources = resourceMapper.listByType(type, offset, size);
        resources.forEach(resource -> setUserOperationStatus(resource, currentUserId));
        return resources;
    }

    @Override
    public List<Resource> getUserResources(Long userId, Integer page, Integer size, Long currentUserId) {
        Integer offset = (page - 1) * size;
        List<Resource> resources = resourceMapper.getUserResources(userId, offset, size);
        resources.forEach(resource -> setUserOperationStatus(resource, currentUserId));
        return resources;
    }

    @Override
    public List<ResourceTypeCountDTO> countResourcesByType() {
        return resourceMapper.countByType();
    }

    @Override
    @Transactional
    public boolean likeResource(Long id, Long userId) {
        // 检查是否已点赞
        if (resourceLikeMapper.exists(id, userId)) {
            throw new BusinessException("已经点赞过了");
        }

        // 添加点赞记录
        ResourceLike like = new ResourceLike();
        like.setResourceId(id);
        like.setUserId(userId);
        resourceLikeMapper.insert(like);

        // 更新资源点赞数
        return resourceMapper.incrementLikes(id) > 0;
    }

    @Override
    @Transactional
    public boolean unlikeResource(Long id, Long userId) {
        // 检查是否已点赞
        if (!resourceLikeMapper.exists(id, userId)) {
            throw new BusinessException("还没有点赞过");
        }

        // 删除点赞记录
        resourceLikeMapper.delete(id, userId);

        // 更新资源点赞数
        return resourceMapper.decrementLikes(id) > 0;
    }

    @Override
    @Transactional
    public boolean downloadResource(Long id, Long userId) {
        // 检查是否已下载
        if (resourceDownloadMapper.exists(id, userId)) {
            throw new BusinessException("已经下载过了");
        }

        // 添加下载记录
        ResourceDownload download = new ResourceDownload();
        download.setResourceId(id);
        download.setUserId(userId);
        resourceDownloadMapper.insert(download);

        // 更新资源下载数
        return resourceMapper.incrementDownloads(id) > 0;
    }

    @Override
    @Transactional
    public boolean reviewResource(Long id, Integer status, String reason) {
        // 添加审核记录
        ResourceReview review = new ResourceReview();
        review.setResourceId(id);
        review.setReviewerId(ThreadLocalUtil.get());
        review.setStatus(status);
        review.setReason(reason);
        resourceReviewMapper.insert(review);

        // 更新资源状态
        return resourceMapper.updateStatus(id, status) > 0;
    }

    @Override
    public boolean incrementViews(Long id) {
        return resourceMapper.incrementViews(id) > 0;
    }

    @Override
    @Transactional
    public boolean batchUpdateStatus(List<Long> ids, Integer status) {
        return resourceMapper.batchUpdateStatus(ids, status) > 0;
    }

    /**
     * 设置用户操作状态（是否已点赞、是否已下载）
     */
    private void setUserOperationStatus(Resource resource, Long currentUserId) {
        if (currentUserId != null) {
            resource.setLiked(resourceLikeMapper.exists(resource.getId(), currentUserId));
            resource.setDownloaded(resourceDownloadMapper.exists(resource.getId(), currentUserId));
        }
    }
}
