package com.jno.cloud.framework.admin.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jno.cloud.framework.admin.user.mapper.UserMapper;
import com.jno.cloud.framework.admin.user.entity.User;
import com.jno.cloud.framework.admin.user.service.UserService;
import com.jno.cloud.framework.util.result.Result;
import com.jno.cloud.framework.util.tool.CommonUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


    @Transactional(readOnly = false, rollbackFor = Exception.class)
    @Override
    public Result register(JSONObject json) {
        User user = json.toJavaObject(User.class);
        user.setId(CommonUtil.GUID());
        System.out.println("用户信息=="+user.toString());
        int result = baseMapper.insert(user);
        if (result>0)
            return new Result(Result.SUCCESS,user,"注册成功");
        else
            return new Result();
    }

    @Override
    public Result login(JSONObject json) {
        User user = new User();
        LambdaQueryWrapper<User> lambda = Wrappers.lambdaQuery();
        if (json.getString("username")!=null && json.getString("username")!=""){
            lambda.eq(User::getUsername,json.getString("username"));
            user = baseMapper.selectOne(lambda);
            if (user==null)
                return new Result("用户不存在");
        }
        if (user.getPassword().equals(json.getString("password"))){
            return new Result(Result.SUCCESS,user,"登陆成功");
        }
        return new Result("用户名或者密码错误");
    }
}
