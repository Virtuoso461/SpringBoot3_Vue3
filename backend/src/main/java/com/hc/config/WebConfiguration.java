package com.hc.config;

import com.hc.common.interceptor.AuthorizeInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author: 何超
 * @date: 2023-05-01 22:38
 * @description: 拦截器
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Autowired
    AuthorizeInterceptor authorizeInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry
                .addInterceptor(authorizeInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/api/auth/**");
    }
}
