package com.jno.cloud.framework.auth.security.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户认证器
 * @author 3hyzy
 * @data 2020-04-15
 **/
@Component
public class AuthFilterManager {

    @Autowired
    private PasswordFilter passwordFilter;
    @Autowired
    private AccountStatusFilter accountStatusFilter;
    @Autowired
    private OnLineCountFilter onLineCountFilter;

    private List<AuthFilter> authfilters;

    @PostConstruct
    void init(){
        authfilters = new ArrayList<>();
        authfilters.add(accountStatusFilter);
        authfilters.add(passwordFilter);
        authfilters.add(onLineCountFilter);
    }

    public void doFilter(Authentication authentication, UserDetails userDetails){
        authfilters.forEach(e->e.Filter(authentication,userDetails));
    }

}
