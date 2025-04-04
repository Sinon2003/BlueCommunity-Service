package com.sinon.bluecommunity.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页数据封装
 * @param <T> 数据类型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageVO<T> {
    
    /**
     * 数据列表
     */
    private List<T> records;
    
    /**
     * 总记录数
     */
    private Long total;
    
    /**
     * 是否有更多数据
     */
    private Boolean hasMore;
    
    public PageVO(List<T> records, Long total) {
        this.records = records;
        this.total = total;
        this.hasMore = false; // 默认没有更多数据
    }
    
    /**
     * 设置是否有更多数据
     * @param pageSize 每页大小
     * @param currentPage 当前页码
     */
    public void setHasMore(int pageSize, int currentPage) {
        this.hasMore = (long) pageSize * currentPage < total;
    }
}
