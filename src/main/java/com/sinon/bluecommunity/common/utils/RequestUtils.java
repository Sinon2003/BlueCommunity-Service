package com.sinon.bluecommunity.common.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

/**
 * 请求工具类
 */
public class RequestUtils {
    private static final List<String> IP_HEADERS = Arrays.asList(
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"
    );

    private static final String UNKNOWN = "unknown";
    private static final String LOCALHOST_IPV4 = "127.0.0.1";
    private static final String LOCALHOST_IPV6 = "0:0:0:0:0:0:0:1";

    private RequestUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 获取当前请求对象
     * @return HttpServletRequest对象
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new IllegalStateException("当前线程中不存在Request上下文");
        }
        return attributes.getRequest();
    }

    /**
     * 获取客户端IP地址
     * @param request HttpServletRequest对象
     * @return IP地址
     */
    public static String getClientIp(HttpServletRequest request) {
        String ip = null;
        
        // 从请求头中查找IP
        for (String header : IP_HEADERS) {
            ip = request.getHeader(header);
            if (!StringUtils.isEmpty(ip) && !UNKNOWN.equalsIgnoreCase(ip)) {
                break;
            }
        }

        // 如果没有找到，使用getRemoteAddr()
        if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        // 处理多个IP的情况，取第一个非unknown的IP
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }

        // 处理本地访问
        if (LOCALHOST_IPV6.equals(ip)) {
            try {
                InetAddress inetAddress = InetAddress.getLocalHost();
                ip = inetAddress.getHostAddress();
            } catch (UnknownHostException e) {
                ip = LOCALHOST_IPV4;
            }
        }

        return ip;
    }

    /**
     * 获取当前请求的完整URL
     * @param request HttpServletRequest对象
     * @return 完整URL
     */
    public static String getFullUrl(HttpServletRequest request) {
        StringBuilder url = new StringBuilder();
        String scheme = request.getScheme();
        int port = request.getServerPort();
        
        url.append(scheme).append("://").append(request.getServerName());
        
        if (("http".equals(scheme) && port != 80) || ("https".equals(scheme) && port != 443)) {
            url.append(":").append(port);
        }
        
        url.append(request.getRequestURI());
        
        String queryString = request.getQueryString();
        if (queryString != null) {
            url.append("?").append(queryString);
        }
        
        return url.toString();
    }

    /**
     * 判断是否为Ajax请求
     * @param request HttpServletRequest对象
     * @return 是否为Ajax请求
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {
        String requestedWith = request.getHeader("X-Requested-With");
        return "XMLHttpRequest".equals(requestedWith);
    }

    /**
     * 获取用户代理字符串
     * @param request HttpServletRequest对象
     * @return 用户代理字符串
     */
    public static String getUserAgent(HttpServletRequest request) {
        return request.getHeader("User-Agent");
    }

    /**
     * 获取请求来源
     * @param request HttpServletRequest对象
     * @return 请求来源
     */
    public static String getReferer(HttpServletRequest request) {
        return request.getHeader("Referer");
    }

    /**
     * 判断是否为移动端访问
     * @param request HttpServletRequest对象
     * @return 是否为移动端访问
     */
    public static boolean isMobileDevice(HttpServletRequest request) {
        String userAgent = getUserAgent(request).toLowerCase();
        return userAgent.matches(".*mobile.*") || userAgent.matches(".*android.*") || 
               userAgent.matches(".*iphone.*") || userAgent.matches(".*ipad.*");
    }
}
