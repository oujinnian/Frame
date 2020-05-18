package com.jno.cloud.framework.admin.user.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jno.cloud.framework.user.entity.User;
import com.jno.cloud.framework.util.result.Result;

public interface UserService extends IService<User> {

    //注册
    Result register(JSONObject json);

    //登陆
    Result login(JSONObject json);

    //查用户
    Result getUser(JSONObject json);

    //新增用户
    Result addUser(JSONObject json);

    //修改用户
    Result editUser(JSONObject json);

    //删除用户
    Result delUser(JSONObject json);



}
