package com.jno.cloud.framework.auth.controller;

import com.jno.cloud.framework.util.result.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @PostMapping("/login")
    @ResponseBody
    public Result login(){
        return new Result(Result.SUCCESS,"","操作成功");
    }

}
