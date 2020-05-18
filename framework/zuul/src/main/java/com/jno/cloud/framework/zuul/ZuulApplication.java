package com.jno.cloud.framework.zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.EnableZuulServer;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@EnableSwagger2 //swagger启动
@EnableZuulServer
@EnableZuulProxy
@EnableDiscoveryClient
//@EnableAuthClient
@ServletComponentScan
@SpringBootApplication
@ComponentScan(basePackages = {"com.jno.cloud.framework.redis","com.jno.cloud.framework.zuul"})
public class ZuulApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZuulApplication.class,args);
    }

}
