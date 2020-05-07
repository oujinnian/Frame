package com.jno.cloud.framework.zuul;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.ComponentScan;

@EnableZuulProxy
@SpringBootApplication
@EnableDiscoveryClient
//@EnableAuthClient
@ServletComponentScan
@ComponentScan(basePackages = {"com.jno.cloud.framework.redis","com.jno.cloud.framework.zuul"})
public class ZuulApplication {
}
