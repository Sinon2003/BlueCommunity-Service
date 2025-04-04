package com.sinon.bluecommunity.common.dto;

import com.sinon.bluecommunity.common.entity.Activity;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 用户个人主页数据传输对象
 */
@Data
public class UserProfileDTO {
    /**
     * 话题数量
     */
    private Integer topicCount;

    /**
     * 评论数量
     */
    private Integer commentCount;

    /**
     * 点赞数量
     */
    private Integer likeCount;

    /**
     * 活动数量
     */
    private Integer activityCount;

    /**
     * 最近活动
     */
    private List<Map<String, Object>> recentActivities;

    /**
     * 最近发布的话题
     */
    private List<Map<String, Object>> recentPosts;

    /**
     * 最近的评论
     */
    private List<Map<String, Object>> recentComments;
}
