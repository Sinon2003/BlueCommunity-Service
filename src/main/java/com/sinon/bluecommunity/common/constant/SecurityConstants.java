package com.sinon.bluecommunity.common.constant;

// 暂时没用到

/**
 * 安全相关常量
 */
public final class SecurityConstants {
    /**
     * Token过期时间（毫秒）
     */
    public static final long TOKEN_EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000L;

    /**
     * Token密钥
     */
    public static final String TOKEN_SECRET = "BlueCommunity-Secret-Key";

    /**
     * Token请求头名称
     */
    public static final String TOKEN_HEADER = "Authorization";

    /**
     * Token前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    private SecurityConstants() {
        throw new IllegalStateException("Constant class");
    }
}
