package com.jno.cloud.framework.auth.security;

import com.alibaba.fastjson.JSONObject;
import com.jno.cloud.framework.redis.util.RedisByteMapper;
import com.jno.cloud.framework.user.entity.User;
import com.jno.cloud.framework.util.encryption.CookieUtil;
import com.jno.cloud.framework.util.result.Result;
import com.jno.cloud.framework.util.tool.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 设置通过请求拦截。登陆成功后处理
 */
@Component("wawAuthenticationSuccessHandler")
@Slf4j
public class SecurityAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final String REDISTOKENKEY = "token_";
    private static final String USERINFOKEY = "userinfokey_";
    @Autowired
    RedisByteMapper redisByteMapper;
    @Value("${login.allow.repeat}")
    private boolean isAllowRepeat;  //是否允许重复登陆
    private Long redisTokenExpire = 120L;



    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        log.info("登陆成功之后，拦截处理");
        //取出token
        String token = (String)getResult(authentication);
        httpServletRequest.getSession().setAttribute("token",token);
        CookieUtil.addCookie(httpServletResponse,null,"/",
                "token",token,-1,false);
        httpServletResponse.setHeader("Conten-Type","application/json;charset=utf-8");
        httpServletResponse.setStatus(200);
        PrintWriter writer = httpServletResponse.getWriter();
        Result result = new Result(Result.SUCCESS,token,"登录成功啦");
        writer.write(JSONObject.toJSONString(result));
        writer.flush();
        writer.close();
    }

    public Object getResult(Authentication authentication){
        //生成token
        String token = CommonUtil.GUID();
        SecurityAuthenticationToken securityToken = (SecurityAuthenticationToken) authentication;
        User user = (User)securityToken.getPrincipal();
        if (!isAllowRepeat){
            //不允许重复登陆，更新redis的数据
//            removeUserInfoByUserId(userJwt.getId());
//            redisByteStrMapper.insertOrUpdateByMinutes(USERINFOKEY+userJwt.getId(),REDISTOKENKEY+token,redisTokenExpire);
        }
//        userInfoSerivce.saveUserInfo(token,toUserInfo(userJwt));
        return token;
    }
//    private UserInfo toUserInfo(UserJwt userJwt){
//        UserInfo userInfo = new UserInfo();
//        return PropertyCopyUtils.copyNullProperties(userJwt,userInfo);
//    }

    /**
     * 删除上次登陆的用户信息 zyy
     * @param userId
     */
    public void removeUserInfoByUserId(String userId) {
        if(redisByteMapper.exist(USERINFOKEY+userId)){
            if (redisByteMapper.select(USERINFOKEY+userId)!=null && redisByteMapper.select(USERINFOKEY+userId)!=""){
                String token = (String) redisByteMapper.select(USERINFOKEY+userId);
//                userInfoSerivce.removeUserInfo(token.replace(REDISTOKENKEY,""));
                redisByteMapper.remove(USERINFOKEY+userId);
            }
        }
    }

}