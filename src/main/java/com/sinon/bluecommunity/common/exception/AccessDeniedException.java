package com.sinon.bluecommunity.common.exception;

import com.sinon.bluecommunity.common.enums.ResultCode;

/**
 * 访问拒绝异常
 */
public class AccessDeniedException extends BusinessException {
    
    public AccessDeniedException(String message) {
        super(ResultCode.FORBIDDEN, message);
    }
    
    public AccessDeniedException() {
        super(ResultCode.FORBIDDEN);
    }
}
