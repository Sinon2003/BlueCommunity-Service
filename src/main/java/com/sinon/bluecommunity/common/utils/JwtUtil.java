package com.sinon.bluecommunity.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.sinon.bluecommunity.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT 工具类
 */
@Slf4j
public class JwtUtil {
    private static final String KEY = "sinon";
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 12;  // 12小时

    private JwtUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 生成用户JWT令牌
     *
     * @param userId   用户ID
     * @param username 用户名
     * @param level    用户等级
     * @return JWT令牌
     */
    public static String generateToken(Long userId, String username, Integer level) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        claims.put("level", level);
        return getToken(claims);
    }

    /**
     * 生成JWT令牌
     *
     * @param claims 自定义声明
     * @return 令牌
     */
    public static String getToken(Map<String, Object> claims) {
        return JWT.create()
                .withClaim("claims", claims)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC256(KEY));
    }

    /**
     * 验证JWT令牌并返回声明
     *
     * @param token 令牌
     * @return 声明
     * @throws BusinessException 如果token无效或已过期
     */
    public static Map<String, Object> parseToken(String token) {
        try {
            return JWT.require(Algorithm.HMAC256(KEY))
                    .build()
                    .verify(token)
                    .getClaim("claims")
                    .asMap();
        } catch (JWTVerificationException e) {
            log.warn("Token解析失败: {}", e.getMessage());
            throw new BusinessException("无效的Token，请重新登录");
        }
    }

    /**
     * 从Token中获取用户ID
     *
     * @param token JWT Token
     * @return 用户ID
     */
    public static Long getUserIdFromToken(String token) {
        Map<String, Object> claims = parseToken(token);
        return Long.valueOf(String.valueOf(claims.get("userId")));
    }

    /**
     * 从Token中获取用户名
     *
     * @param token JWT Token
     * @return 用户名
     */
    public static String getUsernameFromToken(String token) {
        Map<String, Object> claims = parseToken(token);
        return String.valueOf(claims.get("username"));
    }

    /**
     * 从Token中获取用户等级
     *
     * @param token JWT Token
     * @return 用户等级
     */
    public static Integer getLevelFromToken(String token) {
        Map<String, Object> claims = parseToken(token);
        return Integer.valueOf(String.valueOf(claims.get("level")));
    }

    /**
     * 验证Token是否有效
     *
     * @param token JWT Token
     * @return 是否有效
     */
    public static boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (BusinessException e) {
            return false;
        }
    }
}