package com.sinon.bluecommunity.common.utils;

import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 */
public class StringUtils {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_-]{4,16}$");
    private static final int MAX_CONTENT_LENGTH = 1000;

    private StringUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 检查字符串是否为空或null
     * @param str 字符串
     * @return 是否为空
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * 检查字符串是否不为空且不为null
     * @param str 字符串
     * @return 是否不为空
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 生成UUID（去除横线）
     * @return UUID字符串
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * MD5加密
     * @param text 待加密文本
     * @return 加密后的字符串
     */
    public static String md5(String text) {
        return DigestUtils.md5DigestAsHex(text.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 验证邮箱格式
     * @param email 邮箱地址
     * @return 是否有效
     */
    public static boolean isValidEmail(String email) {
        return !isEmpty(email) && EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * 验证用户名格式（4-16位字母、数字、下划线、减号）
     * @param username 用户名
     * @return 是否有效
     */
    public static boolean isValidUsername(String username) {
        return !isEmpty(username) && USERNAME_PATTERN.matcher(username).matches();
    }

    /**
     * 检查内容长度是否超过限制
     * @param content 内容
     * @return 是否超过限制
     */
    public static boolean isContentOverLength(String content) {
        return !isEmpty(content) && content.length() > MAX_CONTENT_LENGTH;
    }

    /**
     * 截取字符串
     * @param str 原字符串
     * @param length 长度
     * @return 截取后的字符串
     */
    public static String truncate(String str, int length) {
        if (isEmpty(str) || str.length() <= length) {
            return str;
        }
        return str.substring(0, length) + "...";
    }

    /**
     * 移除HTML标签
     * @param html HTML字符串
     * @return 纯文本
     */
    public static String removeHtmlTags(String html) {
        if (isEmpty(html)) {
            return html;
        }
        return html.replaceAll("<[^>]+>", "");
    }

    /**
     * 转义HTML特殊字符
     * @param text 原文本
     * @return 转义后的文本
     */
    public static String escapeHtml(String text) {
        if (isEmpty(text)) {
            return text;
        }
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
