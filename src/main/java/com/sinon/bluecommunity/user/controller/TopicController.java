package com.sinon.bluecommunity.user.controller;

import com.sinon.bluecommunity.common.dto.TopicRequestDto;
import com.sinon.bluecommunity.common.entity.Topic;
import com.sinon.bluecommunity.common.utils.ThreadLocalUtil;
import com.sinon.bluecommunity.common.vo.PageVO;
import com.sinon.bluecommunity.common.vo.Result;
import com.sinon.bluecommunity.common.vo.TopicVO;
import com.sinon.bluecommunity.user.service.TopicService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 话题控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/topics")
public class TopicController {

    @Autowired
    private TopicService topicService;

    /**
     * 创建话题
     */
    @PostMapping("/create")
    public Result<Long> createTopic(@Valid @RequestBody TopicRequestDto request) {
        // 从ThreadLocal中获取用户信息
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer userId = (Integer) claims.get("userId");
        System.out.println("userId--------:" + userId);
        if (userId == null) {
            return Result.error("用户未登录");
        }

        Topic topic = new Topic();
        BeanUtils.copyProperties(request, topic);
        topic.setUserId(userId.longValue()); // 转换成Long
        
        Long topicId = topicService.createTopic(topic);
        return Result.success(topicId);
    }

    /**
     * 更新话题
     */
    @PutMapping("/update")
    public Result<Void> updateTopic(@RequestParam Long topicId,
                                  @Valid @RequestBody TopicRequestDto request,
                                  @AuthenticationPrincipal UserDetails userDetails) {
        Topic topic = new Topic();
        BeanUtils.copyProperties(request, topic);
        topic.setId(topicId);
        topic.setUserId(Long.valueOf(userDetails.getUsername()));
        
        topicService.updateTopic(topic);
        return Result.success();
    }

    /**
     * 获取话题详情
     */
    @GetMapping("/detail")
    public Result<TopicVO> getTopicDetail(@RequestParam Long topicId) {
        TopicVO topic = topicService.getTopicDetail(topicId);
        return Result.success(topic);
    }

    /**
     * 分页获取话题列表
     */
    @GetMapping("/list")
    public Result<PageVO<TopicVO>> getTopicList(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String orderBy,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        PageVO<TopicVO> topics = topicService.getTopicList(userId, categoryId, status, keyword, orderBy, page, size);
        return Result.success(topics);
    }

    /**
     * 删除话题
     */
    @DeleteMapping("/delete")
    public Result<Void> deleteTopic(@RequestParam Long topicId,
                                  @AuthenticationPrincipal UserDetails userDetails) {
        topicService.deleteTopic(topicId, Long.valueOf(userDetails.getUsername()));
        return Result.success();
    }

    /**
     * 设置话题置顶状态
     */
    @PutMapping("/pin")
    public Result<Void> updatePinned(@RequestParam Long topicId,
                                   @RequestParam Boolean isPinned,
                                   @AuthenticationPrincipal UserDetails userDetails) {
        topicService.updatePinned(topicId, isPinned, Long.valueOf(userDetails.getUsername()));
        return Result.success();
    }

    /**
     * 点赞话题
     */
    @PostMapping("/like")
    public Result<Void> likeTopic(@RequestParam Long topicId,
                                @AuthenticationPrincipal UserDetails userDetails) {
        topicService.likeTopic(topicId, Long.valueOf(userDetails.getUsername()));
        return Result.success();
    }

    /**
     * 取消点赞
     */
    @DeleteMapping("/unlike")
    public Result<Void> unlikeTopic(@RequestParam Long topicId,
                                  @AuthenticationPrincipal UserDetails userDetails) {
        topicService.unlikeTopic(topicId, Long.valueOf(userDetails.getUsername()));
        return Result.success();
    }

    /**
     * 批量更新话题状态
     */
    @PutMapping("/batch/status")
    public Result<Void> batchUpdateStatus(@RequestParam List<Long> ids,
                                        @RequestParam Integer status,
                                        @AuthenticationPrincipal UserDetails userDetails) {
        topicService.batchUpdateStatus(ids, status, Long.valueOf(userDetails.getUsername()));
        return Result.success();
    }

    /**
     * 获取用户的话题统计
     */
    @GetMapping("/stats/user")
    public Result<TopicVO.UserTopicStats> getUserTopicStats(@AuthenticationPrincipal UserDetails userDetails) {
        TopicVO.UserTopicStats stats = topicService.getUserTopicStats(Long.valueOf(userDetails.getUsername()));
        return Result.success(stats);
    }

    /**
     * 获取分类的话题统计
     */
    @GetMapping("/stats/category")
    public Result<TopicVO.CategoryTopicStats> getCategoryTopicStats(@RequestParam Long categoryId) {
        TopicVO.CategoryTopicStats stats = topicService.getCategoryTopicStats(categoryId);
        return Result.success(stats);
    }
    
    /**
     * 获取热门话题列表
     * 支持按分类筛选，按时间范围筛选，以及不同的热度计算方式
     */
    @GetMapping("/hot")
    public Result<PageVO<TopicVO>> getHotTopics(
            @Parameter(description = "分类ID", required = false)
            @RequestParam(required = false) Long categoryId,
            @Parameter(description = "时间范围,最近 n 天", required = false)
            @RequestParam(required = false) Integer days,
            @Parameter(description = "热度计算方式", required = false)
            @RequestParam(required = false) String hotType,
            @Parameter(description = "页码", required = false)
            @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页大小", required = false)
            @RequestParam(defaultValue = "10") Integer size) {
        PageVO<TopicVO> topics = topicService.getHotTopics(categoryId, days, hotType, page, size);
        return Result.success(topics);
    }
}
