package com.sinon.bluecommunity.common.exception;

import com.sinon.bluecommunity.common.enums.ResultCode;
import lombok.Getter;

/**
 * 业务异常
 */
@Getter
public class BusinessException extends RuntimeException {
    private final int code;
    private final String message;

    public BusinessException(String message) {
        this(ResultCode.INTERNAL_ERROR.getCode(), message);
    }

    public BusinessException(ResultCode resultCode) {
        this(resultCode.getCode(), resultCode.getMessage());
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BusinessException(ResultCode resultCode, String message) {
        this(resultCode.getCode(), message);
    }

    public BusinessException(ResultCode resultCode, Object... args) {
        this(resultCode.getCode(), String.format(resultCode.getMessage(), args));
    }
}
