package com.sinon.bluecommunity.common.exception;

import com.sinon.bluecommunity.common.enums.ResultCode;

/**
 * 认证失败异常
 */
public class AuthenticationFailureException extends BusinessException {
    
    public AuthenticationFailureException(String message) {
        super(ResultCode.UNAUTHORIZED, message);
    }
    
    public AuthenticationFailureException() {
        super(ResultCode.UNAUTHORIZED);
    }
}
