package com.sinon.bluecommunity.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikeRecord {
    private Long id;          // 点赞ID
    private Long userId;      // 用户ID
    private Long postId;      // 帖子ID（可选）
    private Long commentId;   // 评论ID（可选）
    private Date createdAt;   // 点赞时间
}