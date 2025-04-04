package com.sinon.bluecommunity.config;

import com.sinon.bluecommunity.config.interceptors.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")  // 拦截所有路径
                .excludePathPatterns(
                        "/api/user/register",   // 放行注册接口
                        "/api/user/login",      // 放行登录接口
                        "/public/**",            // 放行公共资源
                        "/api/topics/list",      // 放行话题接口
                        "/api/categories/detail",  // 放行分类详情
                        "/api/categories/list",  // 获取分类列表
                        "/swagger-ui.html",      // 放行swagger
                        "/swagger-resources/**", // 放行swagger
                        "/v2/api-docs/**",          // 放行swagger
                        "/swagger-ui/**",  // 放行swagger
                        "/v3/api-docs/**"  // 放行swagger配置
                );
    }
}
