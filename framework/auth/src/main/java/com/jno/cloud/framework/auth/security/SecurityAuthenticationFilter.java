package com.jno.cloud.framework.auth.security;

import com.alibaba.fastjson.JSONObject;
import com.jno.cloud.framework.util.encryption.RSAUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 登录请求接收拦截器
 * @author 3hyzy
 * @data 2020-04-02
 **/
public class SecurityAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public static volatile AtomicInteger onLineCount = new AtomicInteger(0);//在线用户数
    @Value("${auth.freeCode:#{null}}")
    private String freeCode;
    @Value("${auth.loginEncrypt}")
    private boolean isEncrypt;
    private String usernameKey;
    private String passwordKey;
    @Autowired
    private StringRedisTemplate redisTemplate;

    public SecurityAuthenticationFilter() {
        super();
        setLoginUrlMatcher(new AntPathRequestMatcher("/userLogin", "POST"));//登录入口
        setPasswordKey("password");//账号name
        setUsernameKey("username");//密码name
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        } else {
            JSONObject jsonObject;
            try {
                BufferedReader streamReader = new BufferedReader( new InputStreamReader(request.getInputStream(), "UTF-8"));
                StringBuilder responseStrBuilder = new StringBuilder();
                String inputStr;
                while ((inputStr = streamReader.readLine()) != null) {
                    responseStrBuilder.append(inputStr);
                }
                jsonObject = JSONObject.parseObject(responseStrBuilder.toString());
            }catch (Exception e){
                throw new UsernameNotFoundException("请输入正确的账号和密码");
            }
            HttpSession session = request.getSession();
            String privateKey = (String)session.getAttribute("privateKey");

            String username = jsonObject.getString(usernameKey);
            String password = jsonObject.getString(passwordKey);
            String code = jsonObject.getString("code");
            String captchaId = jsonObject.getString("captchaId");

            if(isEncrypt){
                try {
                    username = RSAUtil.decryptByPrivateKey(username,privateKey);
                    password = RSAUtil.decryptByPrivateKey(password,privateKey);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (username == null) {
                username = "";
            }
            if (password == null) {
                password = "";
            }
           /* if(StringUtils.isNotEmpty(freeCode) && !freeCode.equals(code)){
                if(StringUtils.isBlank(captchaId)||StringUtils.isBlank(code)){
                    throw new UsernameNotFoundException("验证码校验失败");
                }
                String redisCode = redisTemplate.opsForValue().get(captchaId);
                if(StringUtils.isBlank(redisCode)){
                    throw new UsernameNotFoundException("验证码已过期，请重新获取");
                }

                if(!redisCode.toLowerCase().equals(code.toLowerCase())) {
                    throw new UsernameNotFoundException("验证码输入错误");
                }
            }
            if(StringUtils.isNotEmpty(captchaId)){
                redisTemplate.delete(captchaId);
            }*/

            username = username.trim();
            SecurityAuthenticationToken authRequest = new SecurityAuthenticationToken(username, password,code,captchaId);
            this.setDetails(request, authRequest);
            return this.getAuthenticationManager().authenticate(authRequest);
        }
    }

    public void setLoginUrlMatcher(RequestMatcher requestMatcher){
        setRequiresAuthenticationRequestMatcher(requestMatcher);
    }

    public String getUsernameKey() {
        return usernameKey;
    }

    public void setUsernameKey(String usernameKey) {
        this.usernameKey = usernameKey;
    }

    public String getPasswordKey() {
        return passwordKey;
    }

    public void setPasswordKey(String passwordKey) {
        this.passwordKey = passwordKey;
    }
    /**
     * 密码至少包含数字、小写字母、大写字母、特殊字符中的三种
     */
    private boolean pwdVerify(String pwd){
        int flag = 0;
        if (pwd.matches(".*[0-9].*")) {
            flag++;
        }
        if (pwd.matches(".*[a-z].*")) {
            flag++;
        }
        if (pwd.matches(".*[A-Z].*")) {
            flag++;
        }
        if (pwd.matches(".*[!/@*#$\\-_()+=&￥].*")) {
            flag++;
        }
        return flag >= 3;
    }
}
