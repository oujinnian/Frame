package com.jno.cloud.framework.auth.security.filter;

import com.jno.cloud.framework.redis.util.RedisByteMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * @author 3hyzy
 * @data 2020-04-15
 **/
@Component
public class AccountStatusFilter implements AuthFilter{
    @Value("${auth.loginCount}")
    public int loginCount;
    @Value("${auth.loginTime}")
    public int loginTime;
    @Value("${auth.loginServlet}")
    public boolean loginServlet;
    @Autowired
    private RedisByteMapper<String> redisByteMapper;

    @Override
    public void Filter(Authentication authentication, UserDetails userDetails) {
        if(userDetails==null){
            throw new UsernameNotFoundException("用户名不存在或被禁用");
        }
        //判断redis中是否有值
        if(loginServlet){
            String errorTime = redisByteMapper.select(userDetails.getUsername()+"_login");
            if(StringUtils.isNotBlank(errorTime) && Integer.valueOf(errorTime)>=loginCount ){
                throw new LockedException("用户名登录次数超过"+loginCount+",请"+loginTime/60+"钟后再试");
            }
        }
    }
}
