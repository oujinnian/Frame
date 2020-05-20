package com.jno.cloud.framework.auth.client.fallback;


import com.alibaba.fastjson.JSONObject;
import com.jno.cloud.framework.auth.client.UserClient;
import com.jno.cloud.framework.util.result.Result;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UserClientFallbackFactory implements FallbackFactory<UserClient> {

    private Logger logger = LoggerFactory.getLogger(UserClientFallbackFactory.class);

    @Override
    public UserClient create(Throwable throwable) {
        logger.error("服务器调用异常：",throwable);
        Result defaultErrorResult = new Result(Result.ERROR,null,throwable.getMessage());

        return new UserClient() {
            @Override
            public Result getUser(JSONObject json) {
                return defaultErrorResult;
            }

            @Override
            public Result addUser(JSONObject json) {
                return defaultErrorResult;
            }

            @Override
            public Result editUser(JSONObject json) {
                return defaultErrorResult;
            }

            @Override
            public Result delUser(JSONObject json) {
                return defaultErrorResult;
            }
        };
    }
}
