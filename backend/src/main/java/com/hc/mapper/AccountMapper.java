package com.hc.mapper;

import com.hc.entity.Account;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: 何超
 * @date: 2023-04-29 16:34
 * @description: 用户接口
 */
@Mapper
public interface AccountMapper {

    /**
     * 根据用户名或邮箱查找
     *
     * @param text
     * @return
     */
    Account findAccountByNameOrEmail(String text);

    /**
     * 新注册用户
     *
     * @param username
     * @param password
     * @param email
     * @return
     */
    int createAccount(String username, String password, String email);

    /**
     * 重置密码
     *
     * @param password
     * @param email
     * @return
     */
    int resetPasswordByEmail(String password, String email);
}
