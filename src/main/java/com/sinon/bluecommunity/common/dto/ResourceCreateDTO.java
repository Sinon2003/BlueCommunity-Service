package com.sinon.bluecommunity.common.dto;

import lombok.Data;
import java.util.List;

/**
 * 资源创建请求DTO
 */
@Data
public class ResourceCreateDTO {
    /**
     * 资源标题
     */
    private String title;

    /**
     * 资源类型
     */
    private String type;

    /**
     * URL类型
     */
    private String urlType;

    /**
     * 资源URL
     */
    private String resourceUrl;

    /**
     * 资源描述
     */
    private String description;

    /**
     * 资源内容
     */
    private String content;

    /**
     * 封面图片URL
     */
    private String coverUrl;

    /**
     * 标签列表
     */
    private List<String> tags;

    /**
     * 验证必填字段
     */
    public boolean isValid() {
        return title != null && !title.trim().isEmpty() &&
               type != null && !type.trim().isEmpty() &&
               urlType != null && !urlType.trim().isEmpty() &&
               resourceUrl != null && !resourceUrl.trim().isEmpty() &&
               description != null && !description.trim().isEmpty() &&
               content != null && !content.trim().isEmpty();
    }
}
