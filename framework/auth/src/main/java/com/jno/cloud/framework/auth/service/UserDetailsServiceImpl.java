package com.jno.cloud.framework.auth.service;

import com.alibaba.fastjson.JSONObject;
import com.jno.cloud.framework.auth.client.UserClient;
import com.jno.cloud.framework.util.result.Result;
import com.jno.cloud.framework.util.tool.CommonUtil;
import com.jno.cloud.framework.util.tool.PropertyCopyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserClient userClient;
    private Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.error("登陆用户输入的用户名：{}",username);

        //根据用户名查找用户
        //调接口获取用户信息
        JSONObject json = new JSONObject();
        json.put("username", username);
        Result result = userClient.getUser(json);
        if (result.getCode() != 0) {
            logger.debug(result.toString());
            return null;
        }
        String js = JSONObject.toJSONString(result.getData());
        com.jno.cloud.framework.user.entity.User user = JSONObject.parseObject(js, com.jno.cloud.framework.user.entity.User.class);

        //密码进行bcrypt加密
        String pwd = user.getPassword();
        String cryptPwd = passwordEncoder.encode(pwd);
        logger.error("加密前："+pwd+"--加密后的密码为：{}",cryptPwd);

        // 这里查询该账户是否过期，这里使用固定代码，假设没有过期
        boolean accountNonExpired = true;
        // 这里查询该账户被删除，假设没有被删除
        boolean enabled = true;
        // 这里查询该账户认证是否过期，假设没有过期
        boolean credentialsNonExpired = true;
        // 查询该账户是否被锁定，假设没有被锁定
        boolean accountNonLocked = true;
        // 关于密码的加密，应该是在创建用户的时候进行的，这里仅仅是举例模拟

        //账号  密码  权限
        return new User(username,cryptPwd, enabled, accountNonExpired,
                credentialsNonExpired, accountNonLocked ,
                AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
    }


}
