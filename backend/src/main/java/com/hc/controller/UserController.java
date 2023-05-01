package com.hc.controller;

import com.hc.common.lang.Result;
import com.hc.entity.vo.AccountUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

/**
 * @author: 何超
 * @date: 2023-05-01 22:34
 * @description:
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    /**
     * 登录用户信息
     *
     * @param user
     * @return
     */
    @GetMapping("/me")
    public Result<AccountUser> me(@SessionAttribute("account") AccountUser user) {
        return Result.success(user);
    }
}
