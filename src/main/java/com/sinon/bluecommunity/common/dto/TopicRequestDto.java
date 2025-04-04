package com.sinon.bluecommunity.common.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * 话题请求DTO
 */
@Data
public class TopicRequestDto {
    
    /**
     * 标题
     */
    @NotBlank(message = "标题不能为空")
    @Size(min = 5, max = 100, message = "标题长度必须在5-100之间")
    private String title;
    
    /**
     * 内容
     */
    @NotBlank(message = "内容不能为空")
    @Size(min = 1, max = 10000, message = "内容长度必须在1-10000之间")
    private String content;
    
    /**
     * 分类ID
     */
    @NotNull(message = "分类不能为空")
    private Long categoryId;
    
    /**
     * 封面图片URL
     */
    private String coverUrl;
    
    /**
     * 状态(0:草稿,1:正常)
     */
    private Integer status;

    /**
     * 标签列表（可选）
     */
    @Size(max = 5, message = "标签数量不能超过5个")
    private List<String> tags;
}
