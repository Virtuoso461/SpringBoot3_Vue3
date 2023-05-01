package com.hc.controller;

import com.hc.common.lang.Constants;
import com.hc.common.lang.Result;
import com.hc.service.AuthorizedService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: 何超
 * @date: 2023-04-30 16:38
 * @description:
 */
@Validated
@RestController
@RequestMapping("/api/auth")
public class AuthorizeController {

    @Autowired
    AuthorizedService authorizedService;

    /**
     * 注册发送邮箱验证码
     *
     * @param email
     * @return
     */
    @PostMapping("/valid-register-email")
    public Result<String> validateRegisterEmail(@Pattern(regexp = Constants.EMAIL_REGEX) @RequestParam("email") String email, HttpSession httpSession) {
        String s = authorizedService.sendValidateEmail(email, httpSession.getId(), false);
        if (s == null) {
            return Result.success("邮箱发送成功，请注意查收！");
        } else {
            return Result.failure(400, s);
        }
    }

    /**
     * 重置密码发送邮箱验证码
     *
     * @param email
     * @param httpSession
     * @return
     */
    @PostMapping("/valid-reset-email")
    public Result<String> validateResetEmail(@Pattern(regexp = Constants.EMAIL_REGEX) @RequestParam("email") String email, HttpSession httpSession) {
        String s = authorizedService.sendValidateEmail(email, httpSession.getId(), true);
        if (s == null) {
            return Result.success("邮箱发送成功，请注意查收！");
        } else {
            return Result.failure(400, s);
        }
    }

    /**
     * 注册新用户
     *
     * @param username
     * @param password
     * @param email
     * @param code
     * @param httpSession
     * @return
     */
    @PostMapping("/register")
    public Result<String> registerUser(@Pattern(regexp = Constants.USERNAME_REGEX) @Length(min = 2, max = 8) @RequestParam("username") String username,
                                       @Length(min = 6, max = 16) @RequestParam("password") String password,
                                       @Pattern(regexp = Constants.EMAIL_REGEX) @RequestParam("email") String email,
                                       @Length(min = 6, max = 6) @RequestParam("code") String code,
                                       HttpSession httpSession) {
        String s = authorizedService.validateAndRegister(username, password, email, code, httpSession.getId());
        if (s == null) {
            return Result.success("恭喜你，注册成功！");
        } else {
            return Result.failure(400, s);
        }
    }

    /**
     * 重置密码验证邮箱
     *
     * @param email
     * @param code
     * @param httpSession
     * @return
     */
    @PostMapping("/start-reset")
    public Result<String> startReset(@Pattern(regexp = Constants.EMAIL_REGEX) @RequestParam("email") String email,
                                     @Length(min = 6, max = 6) @RequestParam("code") String code,
                                     HttpSession httpSession) {
        String s = authorizedService.validateOnly(email, code, httpSession.getId());
        if (s == null) {
            httpSession.setAttribute("reset-password", email);
            return Result.success();
        } else {
            return Result.failure(400, s);
        }
    }

    /**
     * 重置密码
     *
     * @param password
     * @param session
     * @return
     */
    @PostMapping("/do-reset")
    public Result<String> resetPassword(@Length(min = 6, max = 16) @RequestParam("password") String password,
                                        HttpSession session) {
        String email = (String) session.getAttribute("reset-password");
        if (email == null) {
            return Result.failure(401, "请先完成邮箱验证！");
        } else if (authorizedService.resetPassword(password, email)) {
            session.removeAttribute("reset-password");
            return Result.success("密码重置成功！");
        } else {
            return Result.failure(500, "内部错误，请联系管理员！");
        }
    }
}
