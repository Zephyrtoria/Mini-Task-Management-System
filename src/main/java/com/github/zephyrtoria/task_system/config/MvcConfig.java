package com.github.zephyrtoria.task_system.config;

import com.github.zephyrtoria.task_system.interceptor.LoginInterceptor;
import com.github.zephyrtoria.task_system.interceptor.RefreshTokenInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 存在一个order属性，默认为0，按照添加顺序正序执行；或设置order属性值，越低优先级越高
        // 拦截所有请求，token刷新的拦截器
        registry.addInterceptor(new RefreshTokenInterceptor(stringRedisTemplate)).addPathPatterns("/**").order(0);
        // 配置，并排除不需要拦截的url，用户状态拦截器
        registry.addInterceptor(new LoginInterceptor()).excludePathPatterns(
                "/api/users/register",
                "/api/users/login",
                "/swagger-ui/**",
                "/v3/**"
        ).order(1);
    }
}
