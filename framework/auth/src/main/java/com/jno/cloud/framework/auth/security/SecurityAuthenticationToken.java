package com.jno.cloud.framework.auth.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 账号登录信息保存实体
 * @author 3hyzy
 * @data 2020-04-03
 **/
public class SecurityAuthenticationToken extends UsernamePasswordAuthenticationToken{

    private  String code;
    private  String captchaId;


    public SecurityAuthenticationToken(Object principal, Object credentials, String code, String captchaId) {
        super(principal, credentials);
        this.code=code;
        this.captchaId=captchaId;
    }

    public SecurityAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCaptchaId() {
        return captchaId;
    }

    public void setCaptchaId(String captchaId) {
        this.captchaId = captchaId;
    }
}
