package com.jno.cloud.framework.auth.client;

import com.alibaba.fastjson.JSONObject;
import com.jno.cloud.framework.auth.client.fallback.UserClientFallbackFactory;
import com.jno.cloud.framework.util.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@FeignClient(value = "admin", fallbackFactory = UserClientFallbackFactory.class)
public interface UserClient {

    @PostMapping("/user/getUser")
    public Result getUser(@RequestBody JSONObject json);

    @PostMapping("/user/addUser")
    public Result addUser(@RequestBody JSONObject json);

    @PostMapping("/user/editUser")
    public Result editUser(@RequestBody JSONObject json);

    @PostMapping("/user/delUser")
    public Result delUser(@RequestBody JSONObject json);

}
