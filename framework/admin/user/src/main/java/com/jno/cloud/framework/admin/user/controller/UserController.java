package com.jno.cloud.framework.admin.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.jno.cloud.framework.admin.user.service.UserService;
import com.jno.cloud.framework.util.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/register")
    public Result register(JSONObject json){
        return userService.register(json);
    }

    @PostMapping("/login")
    public Result login(JSONObject json){
        return userService.login(json);
    }





}
