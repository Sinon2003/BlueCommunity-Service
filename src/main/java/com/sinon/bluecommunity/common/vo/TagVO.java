package com.sinon.bluecommunity.common.vo;

import lombok.Data;

/**
 * 标签信息展示对象
 */
@Data
public class TagVO {
    /**
     * 标签ID
     */
    private Long id;

    /**
     * 标签名称
     */
    private String name;

    /**
     * 使用次数
     */
    private Integer usageCount;
}
