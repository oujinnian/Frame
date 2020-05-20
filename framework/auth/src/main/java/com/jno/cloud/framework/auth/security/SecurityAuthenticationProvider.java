package com.jno.cloud.framework.auth.security;

import com.jno.cloud.framework.auth.security.filter.AuthFilterManager;
import com.jno.cloud.framework.auth.security.filter.AuthFilterManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

/**
 * 登录验证处理器
 * @author 3hyzy
 * @data 2020-04-01
 **/
@Component
public class SecurityAuthenticationProvider implements AuthenticationProvider {

    //DAO查询用户
    @Qualifier("userDetailsServiceImpl")
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthFilterManager authFilterManager;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //表单输入的用户名
        String username = (String) authentication.getPrincipal();
        //表单输入的密码
        String password = (String) authentication.getCredentials();
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        authFilterManager.doFilter(authentication,userDetails);
        return new SecurityAuthenticationToken(userDetails, authentication, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
