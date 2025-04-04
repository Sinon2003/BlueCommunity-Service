package com.sinon.bluecommunity.common.dto;

import lombok.Data;

/**
 * 资源类型统计DTO
 */
@Data
public class ResourceTypeCountDTO {
    /**
     * 资源类型
     */
    private String type;

    /**
     * 数量
     */
    private Integer count;
}
