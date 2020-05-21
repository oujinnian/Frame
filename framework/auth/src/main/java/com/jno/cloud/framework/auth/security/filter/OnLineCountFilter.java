package com.jno.cloud.framework.auth.security.filter;

import com.jno.cloud.framework.auth.security.SecurityAuthenticationFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
public class OnLineCountFilter  implements AuthFilter {
    @Value("${login.online.limit}")
    private boolean limit; //是否开启在线人数限制
    @Value("${login.online.limit.count}")
    private Integer limitCount; //允许最大在线人数

    @Override
    public void Filter(Authentication authentication, UserDetails userDetails){
        if (limit){
            AtomicInteger onLineCount = SecurityAuthenticationFilter.onLineCount;
            if (onLineCount.get() >= limitCount){
                log.info("当前在线人数超出限制！");
            }
        }
    }
}
