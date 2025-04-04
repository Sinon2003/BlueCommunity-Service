package com.sinon.bluecommunity.user.service.serviceImpl;

import com.sinon.bluecommunity.common.entity.Topic;
import com.sinon.bluecommunity.common.entity.User;
import com.sinon.bluecommunity.common.entity.Tag;
import com.sinon.bluecommunity.common.entity.TagRelation;
import com.sinon.bluecommunity.common.exception.BusinessException;
import com.sinon.bluecommunity.common.vo.PageVO;
import com.sinon.bluecommunity.common.vo.TopicVO;
import com.sinon.bluecommunity.common.vo.TagVO;
import com.sinon.bluecommunity.user.mapper.LikeMapper;
import com.sinon.bluecommunity.user.mapper.TopicMapper;
import com.sinon.bluecommunity.user.mapper.UserMapper;
import com.sinon.bluecommunity.user.mapper.TagMapper;
import com.sinon.bluecommunity.user.mapper.TagRelationMapper;
import com.sinon.bluecommunity.user.service.TopicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 话题服务实现类
 * 提供话题的增删改查、点赞、统计等功能
 * 
 * @author sinon
 * @version 1.0.0
 */
@Slf4j
@Service
public class TopicServiceImpl implements TopicService {

    /**
     * 话题数据访问接口
     */
    @Autowired
    private TopicMapper topicMapper;

    /**
     * 用户数据访问接口
     */
    @Autowired
    private UserMapper userMapper;

    /**
     * 点赞数据访问接口
     */
    @Autowired
    private LikeMapper likeMapper;

    /**
     * 标签数据访问接口
     */
    @Autowired
    private TagMapper tagMapper;

    /**
     * 标签关联数据访问接口
     */
    @Autowired
    private TagRelationMapper tagRelationMapper;

    /**
     * 创建话题
     * 如果未指定状态，默认为正常状态
     */
    @Override
    @Transactional
    public Long createTopic(Topic topic) {
        if (topic == null) {
            throw new BusinessException("话题信息不能为空");
        }
        
        // 1. 设置默认值
        if (topic.getStatus() == null || !topic.getStatus().equals(0) && !topic.getStatus().equals(1)) {
            topic.setStatus(Topic.STATUS_NORMAL);
        }
        // 设置封面URL默认值
        if (topic.getCoverUrl() == null || topic.getCoverUrl().trim().isEmpty()) {
            topic.setCoverUrl(null);  // 显式设置为null，表示无封面
        }
        System.out.println("-----默认值-----success");
        // 2. 保存话题基本信息
        try {
            int rows = topicMapper.insert(topic);
            System.out.println("----rows------success");
            if (rows != 1) {
                throw new BusinessException("创建话题失败");
            }
        } catch (Exception e) {
            log.error("创建话题失败", e);
            throw new BusinessException("创建话题失败：" + e.getMessage());
        }
        
        // 3. 处理标签（如果有）
        if (topic.getTags() != null && !topic.getTags().isEmpty()) {
            for (String tagName : topic.getTags()) {
                // 3.1 获取或创建标签
                Tag tag = tagMapper.selectByName(tagName);
                if (tag == null) {
                    tag = new Tag();
                    tag.setName(tagName);
                    tag.setStatus(1);
                    tag.setUsageCount(0);
                    tagMapper.insert(tag);
                }
                System.out.println("Test--Tag-success");
                
                // 3.2 创建关联关系
                TagRelation relation = new TagRelation();
                relation.setTagId(tag.getId());
                relation.setTargetId(topic.getId());
                relation.setTargetType("topic");
                tagRelationMapper.insert(relation);
                
                // 3.3 更新标签使用计数
                tagMapper.incrementUsageCount(tag.getId());
            }
        }
        
        return topic.getId();
    }

    /**
     * 更新话题
     * 会检查话题是否存在，以及是否有权限更新
     */
    @Override
    @Transactional
    public void updateTopic(Topic topic) {
        if (topic == null || topic.getId() == null) {
            throw new BusinessException("话题信息不能为空");
        }
        
        // 检查话题是否存在
        Topic existingTopic = topicMapper.selectById(topic.getId());
        if (existingTopic == null) {
            throw new BusinessException("话题不存在");
        }
        
        try {
            int rows = topicMapper.update(topic);
            if (rows != 1) {
                throw new BusinessException("更新话题失败");
            }
        } catch (Exception e) {
            log.error("更新话题失败", e);
            throw new BusinessException("更新话题失败：" + e.getMessage());
        }
    }

    /**
     * 获取话题详情
     * 包含话题信息和作者信息
     */
    @Override
    public TopicVO getTopicDetail(Long id) {
        if (id == null) {
            throw new BusinessException("话题ID不能为空");
        }
        
        try {
            // 获取话题信息
            Topic topic = topicMapper.selectById(id);
            if (topic == null) {
                throw new BusinessException("话题不存在");
            }
            
            // 获取作者信息
            User author = userMapper.selectById(topic.getUserId());
            if (author == null) {
                throw new BusinessException("话题作者不存在");
            }
            
            // 转换为VO对象
            TopicVO vo = new TopicVO();
            BeanUtils.copyProperties(topic, vo);
            vo.setAuthor(author);
            
            // 查询话题的标签并转换为TagVO
            List<Tag> tags = tagMapper.selectByTopicId(topic.getId());
            if (!CollectionUtils.isEmpty(tags)) {
                List<TagVO> tagVOs = tags.stream().map(tag -> {
                    TagVO tagVO = new TagVO();
                    BeanUtils.copyProperties(tag, tagVO);
                    return tagVO;
                }).collect(Collectors.toList());
                vo.setTags(tagVOs);
            } else {
                vo.setTags(new ArrayList<>());
            }
            
            return vo;
            
        } catch (Exception e) {
            log.error("获取话题详情失败", e);
            throw new BusinessException("获取话题详情失败：" + e.getMessage());
        }
    }

    /**
     * 分页获取话题列表
     * 支持按用户、分类、状态、关键词搜索
     * 支持多种排序方式
     */
    @Override
    public PageVO<TopicVO> getTopicList(Long userId, Long categoryId, Integer status,
                                       String keyword, String orderBy, Integer page, Integer size) {
        // 参数校验和默认值设置
        page = page == null || page < 1 ? 1 : page;
        size = size == null || size < 1 ? 10 : size;
        
        try {
            // 计算分页偏移量
            int offset = (page - 1) * size;
            
            // 查询数据
            List<Topic> topics = topicMapper.selectList(userId, categoryId, status, keyword, offset, size, orderBy);
            if (CollectionUtils.isEmpty(topics)) {
                return new PageVO<>(new ArrayList<>(), 0L);
            }
            
            // 获取作者信息（批量查询优化）
            final Map<Long, User> userMap;
            List<Long> userIds = topics.stream()
                .map(Topic::getUserId)
                .distinct()  // 去重
                .collect(Collectors.toList());
            
            if (!CollectionUtils.isEmpty(userIds)) {
                List<User> users = userMapper.selectByIds(userIds);
                userMap = users.stream()
                    .collect(Collectors.toMap(
                        User::getId,
                        user -> user,
                        (existing, replacement) -> existing,  // 如果有重复的key，保留第一个
                        HashMap::new
                    ));
            } else {
                userMap = new HashMap<>();
            }
            
            // 转换为VO对象
            List<TopicVO> voList = topics.stream().map(topic -> {
                TopicVO vo = new TopicVO();
                BeanUtils.copyProperties(topic, vo);
                vo.setAuthor(userMap.get(topic.getUserId()));
                
                // 查询话题的标签并转换为TagVO
                List<Tag> tags = tagMapper.selectByTopicId(topic.getId());
                if (!CollectionUtils.isEmpty(tags)) {
                    List<TagVO> tagVOs = tags.stream().map(tag -> {
                        TagVO tagVO = new TagVO();
                        BeanUtils.copyProperties(tag, tagVO);
                        return tagVO;
                    }).collect(Collectors.toList());
                    vo.setTags(tagVOs);
                } else {
                    vo.setTags(new ArrayList<>());
                }
                
                return vo;
            }).collect(Collectors.toList());
            
            // 获取总数
            long total = topicMapper.count(userId, categoryId, status, keyword);
            return new PageVO<>(voList, total);
            
        } catch (Exception e) {
            log.error("获取话题列表失败", e);
            throw new BusinessException("获取话题列表失败：" + e.getMessage());
        }
    }

    /**
     * 删除话题（逻辑删除）
     * 需要验证操作权限
     */
    @Override
    @Transactional
    public void deleteTopic(Long id, Long operatorId) {
        if (id == null || operatorId == null) {
            throw new BusinessException("参数不能为空");
        }
        
        try {
            // 检查权限
            if (!checkPermission(operatorId, id)) {
                throw new BusinessException("无权限操作");
            }
            
            // 更新状态为删除
            int rows = topicMapper.updateStatus(id, Topic.STATUS_DELETED);
            if (rows != 1) {
                throw new BusinessException("删除话题失败");
            }
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("删除话题失败", e);
            throw new BusinessException("删除话题失败：" + e.getMessage());
        }
    }

    /**
     * 更新话题置顶状态
     * 需要验证操作权限（通常需要管理员权限）
     */
    @Override
    @Transactional
    public void updatePinned(Long id, Boolean isPinned, Long operatorId) {
        if (id == null || isPinned == null || operatorId == null) {
            throw new BusinessException("参数不能为空");
        }
        
        try {
            // 检查权限（这里可能需要检查是否是管理员）
            if (!checkPermission(operatorId, id)) {
                throw new BusinessException("无权限操作");
            }
            
            int rows = topicMapper.updatePinned(id, isPinned);
            if (rows != 1) {
                throw new BusinessException("更新置顶状态失败");
            }
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("更新置顶状态失败", e);
            throw new BusinessException("更新置顶状态失败：" + e.getMessage());
        }
    }

    /**
     * 增加话题浏览量
     */
    @Override
    public void incrementViews(Long id) {
        if (id == null) {
            throw new BusinessException("话题ID不能为空");
        }
        
        try {
            int rows = topicMapper.incrementViews(id);
            if (rows != 1) {
                throw new BusinessException("更新浏览量失败");
            }
        } catch (Exception e) {
            log.error("更新浏览量失败", e);
            throw new BusinessException("更新浏览量失败：" + e.getMessage());
        }
    }

    /**
     * 点赞话题
     * 包含重复点赞检查
     */
    @Override
    @Transactional
    public void likeTopic(Long topicId, Long userId) {
        if (topicId == null || userId == null) {
            throw new BusinessException("参数不能为空");
        }
        
        try {
            // 检查话题是否存在
            Topic topic = topicMapper.selectById(topicId);
            if (topic == null) {
                throw new BusinessException("话题不存在");
            }
            
            // 检查是否已点赞
            if (likeMapper.exists(userId, topicId, "topic")) {
                throw new BusinessException("已经点赞过了");
            }
            
            // 添加点赞记录
            likeMapper.insert(userId, topicId, "topic");
            
            // 更新话题点赞数
            topicMapper.updateLikes(topicId, 1);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("点赞话题失败", e);
            throw new BusinessException("点赞话题失败：" + e.getMessage());
        }
    }

    /**
     * 取消点赞
     * 包含点赞记录检查
     */
    @Override
    @Transactional
    public void unlikeTopic(Long topicId, Long userId) {
        if (topicId == null || userId == null) {
            throw new BusinessException("参数不能为空");
        }
        
        try {
            // 检查是否已点赞
            if (!likeMapper.exists(userId, topicId, "topic")) {
                throw new BusinessException("还没有点赞过");
            }
            
            // 删除点赞记录
            likeMapper.delete(userId, topicId, "topic");
            
            // 更新话题点赞数
            topicMapper.updateLikes(topicId, -1);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("取消点赞失败", e);
            throw new BusinessException("取消点赞失败：" + e.getMessage());
        }
    }

    /**
     * 检查用户是否有权限操作话题
     * 目前仅检查是否为作者，后续可扩展管理员权限
     */
    @Override
    public boolean checkPermission(Long userId, Long topicId) {
        if (userId == null || topicId == null) {
            return false;
        }
        
        try {
            Topic topic = topicMapper.selectById(topicId);
            if (topic == null) {
                return false;
            }
            
            // 检查是否是作者
            return topic.getUserId().equals(userId);
        } catch (Exception e) {
            log.error("检查话题权限失败", e);
            return false;
        }
    }

    /**
     * 获取用户的话题统计信息
     * 包括总数、已发布数、草稿数
     */
    @Override
    public TopicVO.UserTopicStats getUserTopicStats(Long userId) {
        if (userId == null) {
            throw new BusinessException("用户ID不能为空");
        }
        
        try {
            TopicVO.UserTopicStats stats = new TopicVO.UserTopicStats();
            stats.setTotalCount(topicMapper.countByUser(userId, null));
            stats.setPublishedCount(topicMapper.countByUser(userId, Topic.STATUS_NORMAL));
            stats.setDraftCount(topicMapper.countByUser(userId, Topic.STATUS_DRAFT));
            
            return stats;
        } catch (Exception e) {
            log.error("获取用户话题统计失败", e);
            throw new BusinessException("获取用户话题统计失败：" + e.getMessage());
        }
    }

    /**
     * 批量更新话题状态
     * 需要验证每个话题的操作权限
     */
    @Override
    @Transactional
    public void batchUpdateStatus(List<Long> ids, Integer status, Long operatorId) {
        if (CollectionUtils.isEmpty(ids) || status == null || operatorId == null) {
            throw new BusinessException("参数不能为空");
        }
        
        try {
            // 检查权限
            for (Long id : ids) {
                if (!checkPermission(operatorId, id)) {
                    throw new BusinessException("无权限操作部分话题");
                }
            }
            
            int rows = topicMapper.batchUpdateStatus(ids, status);
            if (rows != ids.size()) {
                throw new BusinessException("批量更新状态失败");
            }
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("批量更新话题状态失败", e);
            throw new BusinessException("批量更新话题状态失败：" + e.getMessage());
        }
    }

    /**
     * 获取分类的话题统计信息
     * 包括总数和正常状态的数量
     */
    @Override
    public TopicVO.CategoryTopicStats getCategoryTopicStats(Long categoryId) {
        if (categoryId == null) {
            throw new BusinessException("分类ID不能为空");
        }
        
        try {
            TopicVO.CategoryTopicStats stats = new TopicVO.CategoryTopicStats();
            stats.setTotalCount(topicMapper.countByCategory(categoryId, null));
            stats.setNormalCount(topicMapper.countByCategory(categoryId, Topic.STATUS_NORMAL));
            
            return stats;
        } catch (Exception e) {
            log.error("获取分类话题统计失败", e);
            throw new BusinessException("获取分类话题统计失败：" + e.getMessage());
        }
    }
}
