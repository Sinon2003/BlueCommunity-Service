package com.sinon.bluecommunity.common.vo;

import lombok.Data;
import java.util.List;

/**
 * 分页结果封装类
 * @param <T> 数据类型
 */
@Data
public class PageResult<T> {
    /**
     * 当前页码
     */
    private int page;
    
    /**
     * 每页数量
     */
    private int size;
    
    /**
     * 总记录数
     */
    private long total;
    
    /**
     * 总页数
     */
    private int totalPages;
    
    /**
     * 数据列表
     */
    private List<T> list;
    
    /**
     * 是否为第一页
     */
    private boolean isFirst;
    
    /**
     * 是否为最后一页
     */
    private boolean isLast;
    
    public PageResult(List<T> list, int page, int size, long total) {
        this.list = list;
        this.page = page;
        this.size = size;
        this.total = total;
        this.totalPages = size == 0 ? 1 : (int) Math.ceil((double) total / (double) size);
        this.isFirst = page <= 1;
        this.isLast = page >= totalPages;
    }
}
