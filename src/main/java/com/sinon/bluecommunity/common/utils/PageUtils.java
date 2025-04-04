package com.sinon.bluecommunity.common.utils;

import com.sinon.bluecommunity.common.constant.CommonConstants;
import com.sinon.bluecommunity.common.exception.BusinessException;
import com.sinon.bluecommunity.common.vo.PageResult;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * 分页工具类
 */
public class PageUtils {
    private PageUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 验证分页参数
     * @param page 页码
     * @param size 每页大小
     */
    public static void validatePageParams(int page, int size) {
        if (page < 1) {
            throw new BusinessException("页码必须大于0");
        }
        if (size < 1 || size > CommonConstants.MAX_PAGE_SIZE) {
            throw new BusinessException("每页大小必须在1-" + CommonConstants.MAX_PAGE_SIZE + "之间");
        }
    }

    /**
     * 获取分页偏移量
     * @param page 页码
     * @param size 每页大小
     * @return 偏移量
     */
    public static int getOffset(int page, int size) {
        return (page - 1) * size;
    }

    /**
     * 创建空的分页结果
     * @param page 页码
     * @param size 每页大小
     * @return 空的分页结果
     * @param <T> 数据类型
     */
    public static <T> PageResult<T> emptyPage(int page, int size) {
        return new PageResult<>(Collections.emptyList(), page, size, 0);
    }

    /**
     * 创建分页结果
     * @param list 数据列表
     * @param page 页码
     * @param size 每页大小
     * @param total 总数
     * @return 分页结果
     * @param <T> 数据类型
     */
    public static <T> PageResult<T> createPageResult(List<T> list, int page, int size, long total) {
        if (CollectionUtils.isEmpty(list)) {
            return emptyPage(page, size);
        }
        return new PageResult<>(list, page, size, total);
    }

    /**
     * 获取总页数
     * @param total 总记录数
     * @param size 每页大小
     * @return 总页数
     */
    public static int getTotalPages(long total, int size) {
        if (total <= 0) {
            return 0;
        }
        return (int) Math.ceil((double) total / size);
    }

    /**
     * 检查页码是否有效
     * @param page 页码
     * @param totalPages 总页数
     * @return 是否有效
     */
    public static boolean isValidPage(int page, int totalPages) {
        return page > 0 && page <= totalPages;
    }
}
