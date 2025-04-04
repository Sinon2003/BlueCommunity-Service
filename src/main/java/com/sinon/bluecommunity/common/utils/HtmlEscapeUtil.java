package com.sinon.bluecommunity.common.utils;

import org.springframework.web.util.HtmlUtils;

/**
 * HTML转义工具类
 */
public class HtmlEscapeUtil {
    
    /**
     * 转义HTML内容
     * 将HTML特殊字符转换为对应的实体引用
     */
    public static String escapeHtml(String content) {
        if (content == null) {
            return null;
        }
        return HtmlUtils.htmlEscape(content);
    }

    /**
     * 反转义HTML内容
     * 将HTML实体引用转换回特殊字符
     */
    public static String unescapeHtml(String content) {
        if (content == null) {
            return null;
        }
        return HtmlUtils.htmlUnescape(content);
    }
}
