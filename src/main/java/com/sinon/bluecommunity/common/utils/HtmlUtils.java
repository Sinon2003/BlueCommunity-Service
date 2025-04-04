package com.sinon.bluecommunity.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

/**
 * HTML工具类，用于XSS过滤
 */
public class HtmlUtils {
    
    /**
     * 清理XSS攻击
     *
     * @param value 待清理的字符串
     * @return 清理后的字符串
     */
    public static String cleanXSS(String value) {
        if (StringUtils.isBlank(value)) {
            return value;
        }
        
        // 使用jsoup的清理功能
        value = Jsoup.clean(value, Safelist.none());
        
        // 过滤特殊字符
        value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
        value = value.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
        value = value.replaceAll("'", "&#39;");
        value = value.replaceAll("eval\\((.*)\\)", "");
        value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
        value = value.replaceAll("script", "");
        
        return value.trim();
    }

    /**
     * 判断内容是否包含XSS攻击
     *
     * @param value 待检查的字符串
     * @return true if contains XSS
     */
    public static boolean containsXSS(String value) {
        if (StringUtils.isBlank(value)) {
            return false;
        }
        
        // 清理后的内容与原内容比较
        String cleaned = cleanXSS(value);
        return !cleaned.equals(value);
    }

    /**
     * 将HTML转义为文本
     *
     * @param html HTML内容
     * @return 文本内容
     */
    public static String htmlToText(String html) {
        if (StringUtils.isBlank(html)) {
            return html;
        }
        return Jsoup.parse(html).text();
    }
}
