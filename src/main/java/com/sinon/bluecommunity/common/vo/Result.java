package com.sinon.bluecommunity.common.vo;

import com.sinon.bluecommunity.common.enums.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一响应类
 * @param <T> 泛型参数
 */
@NoArgsConstructor // 无参构造方法
@AllArgsConstructor // 全参构造方法
@Data // 自动生成Getter、Setter、toString等方法
public class Result<T> {
    private int code; // 响应码
    private String message; // 响应消息
    private T data; // 响应数据

    /**
     * 响应成功（有数据）
     */
    public static <E> Result<E> success(E data) {
        return new Result<>(200, "操作成功", data);
    }

    /**
     * 响应成功（无数据）
     */
    public static <E> Result<E> success() {
        return new Result<>(200, "操作成功", null);
    }

    /**
     * 响应失败（带消息）
     */
    public static <E> Result<E> error(String message) {
        return new Result<>(400, message, null);
    }

    /**
     * 响应失败（带状态码和消息）
     */
    public static <E> Result<E> error(int code, String message) {
        return new Result<>(code, message, null);
    }

    /**
     * 响应失败（使用ResultCode）
     */
    public static <E> Result<E> error(ResultCode resultCode) {
        return new Result<>(resultCode.getCode(), resultCode.getMessage(), null);
    }

    /**
     * 响应失败（使用ResultCode和自定义消息）
     */
    public static <E> Result<E> error(ResultCode resultCode, String message) {
        return new Result<>(resultCode.getCode(), message, null);
    }
}
