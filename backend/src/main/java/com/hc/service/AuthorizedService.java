package com.hc.service;

import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author: 何超
 * @date: 2023-04-30 16:22
 * @description:
 */
public interface AuthorizedService extends UserDetailsService {

    /**
     * 发送邮箱验证码
     *
     * @param email
     * @param sessionId
     * @param hasAccount
     * @return
     */
    String sendValidateEmail(String email, String sessionId, boolean hasAccount);

    /**
     * 验证码邮箱并注册
     *
     * @param username
     * @param password
     * @param email
     * @param code
     * @param sessionId
     * @return
     */
    String validateAndRegister(String username, String password, String email, String code, String sessionId);

    /**
     * 验证验证码是否正确
     *
     * @param email
     * @param code
     * @param sessionId
     * @return
     */
    String validateOnly(String email, String code, String sessionId);

    /**
     * 重置密码
     *
     * @param password
     * @param email
     * @return
     */
    boolean resetPassword(String password, String email);
}
