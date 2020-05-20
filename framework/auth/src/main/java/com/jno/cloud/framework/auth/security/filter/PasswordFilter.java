package com.jno.cloud.framework.auth.security.filter;

import com.jno.cloud.framework.auth.security.SecurityAuthenticationToken;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author 3hyzy
 * @data 2020-04-15
 **/
@Component
public class PasswordFilter implements AuthFilter{

    //密码加密解密
    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Value("${auth.freeCode:#{null}}")
    private String freeCode;

    @Override
    public void Filter(Authentication authentication, UserDetails userDetails) {
        if(!bCryptPasswordEncoder.matches((String)authentication.getCredentials(), userDetails.getPassword())){
            throw new BadCredentialsException("密码错误");
        }
        if(authentication instanceof SecurityAuthenticationToken){
            SecurityAuthenticationToken auth=(SecurityAuthenticationToken)authentication;
            if(StringUtils.isNotEmpty(freeCode) && !freeCode.equals(auth.getCode())){
                if(StringUtils.isBlank(auth.getCaptchaId())||StringUtils.isBlank(auth.getCode())){
                    throw new BadCredentialsException("验证码校验失败");
                }
                String redisCode = redisTemplate.opsForValue().get(auth.getCaptchaId());
                if(StringUtils.isBlank(redisCode)){
                    throw new BadCredentialsException("验证码已过期，请重新获取");
                }

                if(!redisCode.toLowerCase().equals(auth.getCode().toLowerCase())) {
                    throw new BadCredentialsException("验证码输入错误");
                }
            }
            if(StringUtils.isNotEmpty(auth.getCaptchaId())){
                redisTemplate.delete(auth.getCaptchaId());
            }
        }
    }
}
