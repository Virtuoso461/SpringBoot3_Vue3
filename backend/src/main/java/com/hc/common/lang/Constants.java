package com.hc.common.lang;

/**
 * @author: 何超
 * @date: 2023-05-01 19:02
 * @description: 常量类
 */
public interface Constants {

    /**
     * 注册邮箱验证码
     */
    String EMAIL_REGISTER_KEY = "email_";

    /**
     * 邮箱验证正则表达式
     */
    String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,}$";

    /**
     * 用户名验证正则表达式
     */
    String USERNAME_REGEX = "^[a-zA-Z0-9\\u4e00-\\u9fa5]+$";
}
