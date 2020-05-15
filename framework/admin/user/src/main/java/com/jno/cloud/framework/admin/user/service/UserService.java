package com.jno.cloud.framework.admin.user.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jno.cloud.framework.admin.user.entity.User;
import com.jno.cloud.framework.util.result.Result;

public interface UserService extends IService<User> {

    //注册
    Result register(JSONObject json);

    //登陆
    Result login(JSONObject json);
}
