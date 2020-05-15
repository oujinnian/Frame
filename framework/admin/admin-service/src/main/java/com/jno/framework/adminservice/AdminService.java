package com.jno.framework.adminservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScans;

@SpringBootApplication
@EnableDiscoveryClient  //该注解可以将微服务注册到nacos上面，微服务必须
@MapperScan({"com.jno.cloud.framework.*.*.mapper","com.jno.cloud.framework.*.mapper"})
public class AdminService {

    public static void main(String[] args) {
        SpringApplication.run(AdminService.class,args);
    }

}
