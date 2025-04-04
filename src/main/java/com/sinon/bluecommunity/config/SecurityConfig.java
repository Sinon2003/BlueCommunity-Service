package com.sinon.bluecommunity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 安全配置类
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    /**
     * 密码加密器
     * 使用BCrypt加密算法，这是一种单向Hash加密算法，
     * 每次加密的结果都不一样，但是可以用于验证
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12); // 设置加密强度为12
    }

    /**
     * 配置Spring Security的安全过滤链
     * 由于使用了自定义的JWT拦截器，这里只配置基本的安全规则
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 禁用CSRF，因为我们使用JWT进行身份验证
            .csrf(AbstractHttpConfigurer::disable)
            // 禁用默认的登录表单，因为我们使用JWT
            .formLogin(AbstractHttpConfigurer::disable)
            // 禁用HTTP Basic认证
            .httpBasic(AbstractHttpConfigurer::disable)
            // 配置请求授权规则
            .authorizeHttpRequests(auth -> auth
                // 允许所有请求通过，认证由自定义拦截器处理
                .anyRequest().permitAll()
            );
        
        return http.build();
    }
}
