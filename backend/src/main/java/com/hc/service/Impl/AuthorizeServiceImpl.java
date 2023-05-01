package com.hc.service.Impl;

import com.hc.common.lang.Constants;
import com.hc.entity.Account;
import com.hc.mapper.AccountMapper;
import com.hc.service.AuthorizedService;
import com.hc.util.ValidateCodeUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author: 何超
 * @date: 2023-04-29 16:33
 * @description: Security 自定义登录逻辑
 */
@Service
public class AuthorizeServiceImpl implements AuthorizedService {

    @Value("${spring.mail.username}")
    public String from;
    @Autowired
    AccountMapper accountMapper;
    @Resource
    JavaMailSender javaMailSender;

    @Resource
    StringRedisTemplate template;

    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    /**
     * 登录验证
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if (username == null) {
            throw new UsernameNotFoundException("用户名不能为空！");
        }
        Account account = accountMapper.findAccountByNameOrEmail(username);
        if (account == null) {
            throw new UsernameNotFoundException("用户名或密码错误！");
        }
        return User
                .withUsername(account.getUsername())
                .password(account.getPassword())
                .roles("user")
                .build();
    }

    /**
     * 发送邮箱验证码
     *
     * @param email
     * @return
     */
    @Override
    public String sendValidateEmail(String email, String id, boolean hasAccount) {
        String key = Constants.EMAIL_REGISTER_KEY + id + "_" + email + ":" + hasAccount;
        if (template.hasKey(key)) {
            Long expire = Optional.ofNullable(template.getExpire(key, TimeUnit.SECONDS)).orElse(0L);
            if (expire > 120) return "请求频繁，请稍后再试！";
        }
        Account account = accountMapper.findAccountByNameOrEmail(email);
        if (hasAccount && account == null) {
            return "没有此邮箱地址的账户！";
        }
        if (!hasAccount && account != null) {
            return "此邮箱已被其他用户注册！";
        }
        Integer code = ValidateCodeUtils.generateValidateCode(6);
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(from);
        mailMessage.setTo(email);
        mailMessage.setSubject("邮箱验证信息");
        mailMessage.setText("验证码是：" + code);
        try {
            javaMailSender.send(mailMessage);
            template.opsForValue().set(key, String.valueOf(code), 3, TimeUnit.MINUTES);
            return null;
        } catch (MailException e) {
            e.printStackTrace();
            return "邮件发送失败，请检查邮件地址是否有效！";
        }
    }

    /**
     * 验证码邮箱并注册
     *
     * @param username
     * @param password
     * @param email
     * @param code
     * @return
     */
    @Override
    public String validateAndRegister(String username, String password, String email, String code, String id) {
        String key = Constants.EMAIL_REGISTER_KEY + id + "_" + email + ":false";
        if (template.hasKey(key)) {
            String s = template.opsForValue().get(key);
            if (s == null) {
                return "验证码失效，请重新登录！";
            }
            if (s.equals(code)) {
                Account account = accountMapper.findAccountByNameOrEmail(username);
                if (account != null) return "此用户名已被注册，请更好用户名试试！";
                password = bCryptPasswordEncoder.encode(password);
                template.delete(Constants.EMAIL_REGISTER_KEY);
                if (accountMapper.createAccount(username, password, email) > 0) {
                    return null;
                } else {
                    return "内部错误，请联系管理员！";
                }
            } else {
                return "验证码错误，请检查后再提交！";
            }
        } else {
            return "请先请求一封验证码邮件！";
        }
    }

    /**
     * 验证验证码是否正确
     *
     * @param email
     * @param code
     * @param sessionId
     * @return
     */
    @Override
    public String validateOnly(String email, String code, String sessionId) {
        String key = Constants.EMAIL_REGISTER_KEY + sessionId + "_" + email + ":true";
        if (template.hasKey(key)) {
            String s = template.opsForValue().get(key);
            if (s == null) {
                return "验证码已失效，请重新登录！";
            }
            if (s.equals(code)) {
                return null;
            } else {
                return "验证码邮箱失败，请检查后再试！";
            }
        } else {
            return "请先请求一封验证码邮件！";
        }
    }

    /**
     * 重置密码
     *
     * @param password
     * @param email
     * @return
     */
    @Override
    public boolean resetPassword(String password, String email) {
        password = bCryptPasswordEncoder.encode(password);
        return accountMapper.resetPasswordByEmail(password, email) > 0;
    }
}
