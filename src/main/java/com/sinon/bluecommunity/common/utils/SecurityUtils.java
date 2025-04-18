package com.sinon.bluecommunity.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

// 暂时弃用
/**
 * 安全工具类 - 密码加密工具
 */
@Slf4j
public class SecurityUtils {
    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    private SecurityUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 加密密码
     * @param password 原始密码
     * @return 加密后的密码
     */
    public static String encryptPassword(String password) {
        return PASSWORD_ENCODER.encode(password);
    }

    /**
     * 验证密码
     * @param rawPassword 原始密码
     * @param encodedPassword 加密后的密码
     * @return 是否匹配
     */
    public static boolean matchesPassword(String rawPassword, String encodedPassword) {
        return PASSWORD_ENCODER.matches(rawPassword, encodedPassword);
    }
}
