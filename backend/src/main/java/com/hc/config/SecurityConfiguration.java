package com.hc.config;

import com.alibaba.fastjson.JSONObject;
import com.hc.common.lang.Result;
import com.hc.service.AuthorizedService;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * @author: 何超
 * @date: 2023-04-28 21:52
 * @description: Security 配置类
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    AuthorizedService authorizedService;

    @Resource
    DataSource dataSource;

    /**
     * Security 处理链
     *
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, PersistentTokenRepository tokenRepository) throws Exception {
        return http
                .authorizeHttpRequests()
                .requestMatchers("/api/auth/**")
                .permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginProcessingUrl("/api/auth/login")
                .successHandler(this::onAuthenticationSuccess)
                .failureHandler(this::onAuthenticationFailure)
                .and()
                .logout()
                .logoutUrl("/api/auth/logout")
                .logoutSuccessHandler(this::onAuthenticationSuccess)
                .and()
                .rememberMe()
                .rememberMeParameter("remember")
                .tokenRepository(tokenRepository)
                .tokenValiditySeconds(3600 * 24 * 7)
                .and()
                .csrf()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint(this::commence)
                .and()
                .cors()
                .configurationSource(this.configurationSource())
                .and()
                .build();
    }

    /**
     * 记住我功能
     *
     * @return
     */
    @Bean
    public PersistentTokenRepository tokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        jdbcTokenRepository.setCreateTableOnStartup(false);
        return jdbcTokenRepository;
    }

    /**
     * 跨域配置
     *
     * @return
     */
    private CorsConfigurationSource configurationSource() {
        CorsConfiguration cors = new CorsConfiguration();
        cors.addAllowedOriginPattern("*");
        cors.setAllowCredentials(true);
        cors.addAllowedHeader("*");
        cors.addAllowedMethod("*");
        cors.addExposedHeader("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cors);
        return source;
    }

    /**
     * 用户登录认证
     *
     * @param httpSecurity
     * @return
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(authorizedService)
                .and()
                .build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 登录或退出登录成功处理
     *
     * @param request
     * @param response
     * @param authentication
     * @throws IOException
     * @throws ServletException
     */
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        if (request.getRequestURI().endsWith("/login")) {
            response.getWriter().write(JSONObject.toJSONString(Result.success("登录成功！")));
        } else if (request.getRequestURI().endsWith("/logout")) {
            response.getWriter().write(JSONObject.toJSONString(Result.success("退出登录成功！")));
        }
    }

    /**
     * 登录失败处理
     *
     * @param request
     * @param response
     * @param exception
     * @throws IOException
     * @throws ServletException
     */
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(JSONObject.toJSONString(Result.failure(401, exception.getMessage())));
    }

    /**
     * 没有权限时处理
     *
     * @param request
     * @param response
     * @param authException
     * @throws IOException
     * @throws ServletException
     */
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(JSONObject.toJSONString(Result.failure(401, authException.getMessage())));
    }
}
