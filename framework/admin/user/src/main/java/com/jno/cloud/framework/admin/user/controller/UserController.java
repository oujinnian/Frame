package com.jno.cloud.framework.admin.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.protobuf.ExperimentalApi;
import com.jno.cloud.framework.admin.user.service.UserService;
import com.jno.cloud.framework.util.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Api(value = "用户接口", description = "用户信息操作")
public class UserController {

    @Autowired
    private UserService userService;



    @PostMapping("/register")
    public Result register(@RequestBody JSONObject json){
        return userService.register(json);
    }

    @PostMapping("/login")
    public Result login(@RequestBody JSONObject json){
        return userService.login(json);
    }

    @PostMapping("/getUser")
    @ApiOperation(value = "/getUser", notes = "获取一个用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", dataType = "String", required = true)
    })
    public Result getUser(@RequestBody JSONObject json){
        return userService.getUser(json);
    }

    @PostMapping("/addUser")
    public Result addUser(@RequestBody JSONObject json){
        return userService.addUser(json);
    }

    @PostMapping("/editUser")
    public Result editUser(@RequestBody JSONObject json){
        return userService.editUser(json);
    }

    @PostMapping("/delUser")
    public Result delUser(@RequestBody JSONObject json){
        return userService.delUser(json);
    }





}
