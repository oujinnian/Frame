package com.jno.cloud.framework;

import com.codingapi.txlcn.tc.config.EnableDistributedTransaction;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
@EnableDiscoveryClient  //该注解可以将微服务注册到nacos上面，微服务必须
@EnableFeignClients //启用feign客户端
@MapperScan({"com.jno.cloud.framework.*.*.mapper","com.jno.cloud.framework.*.mapper"})
@EnableDistributedTransaction
public class AdminService {

    public static void main(String[] args) {
        SpringApplication.run(AdminService.class,args);
    }

}
