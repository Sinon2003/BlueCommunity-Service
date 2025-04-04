package com.sinon.bluecommunity.config.interceptors;

import com.sinon.bluecommunity.common.utils.JwtUtil;
import com.sinon.bluecommunity.common.utils.ThreadLocalUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

/**
 * 登录拦截器 - 用于处理 HTTP 请求的身份验证
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取 token
        String token = request.getHeader("Authorization");
        
        // 验证token格式
        if (token == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("未提供认证令牌");
            return false;
        }
        
        if (!token.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("无效的令牌格式，应以'Bearer '开头");
            return false;
        }

        // 去掉 "Bearer " 前缀
        token = token.substring(7);

        try {
            // 解析 token 并获取用户信息
            Map<String, Object> claims = JwtUtil.parseToken(token);
            Integer userId = (Integer) claims.get("userId");

            if (userId == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("令牌解析失败");
                return false;
            }

            // 检查 token 是否存在于 Redis 中
            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
            String redisToken = operations.get("TOKEN_" + userId);

            if (redisToken == null || !redisToken.equals(token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("身份验证失败，请重新登录");
                return false;
            }

            // 将用户信息存储到 ThreadLocal 中
            ThreadLocalUtil.set(claims);
            return true;
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("令牌验证失败: " + e.getMessage());
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 清空线程数据，避免内存泄漏
        ThreadLocalUtil.remove();
    }
}