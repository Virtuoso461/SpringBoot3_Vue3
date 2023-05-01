package com.hc.common.interceptor;

import com.hc.entity.vo.AccountUser;
import com.hc.mapper.AccountMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author: 何超
 * @date: 2023-05-01 22:37
 * @description: 拦截器
 */
@Component
public class AuthorizeInterceptor implements HandlerInterceptor {

    @Autowired
    AccountMapper accountMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        User user = (User) authentication.getPrincipal();
        String username = user.getUsername();
        AccountUser accountUser = accountMapper.findAccountUserByNameOrEmail(username);
        request.getSession().setAttribute("account", accountUser);
        return true;
    }
}
