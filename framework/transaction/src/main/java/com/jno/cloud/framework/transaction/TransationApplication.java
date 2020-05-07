package com.jno.cloud.framework.transaction;

import com.codingapi.txlcn.tm.config.EnableTransactionManagerServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableTransactionManagerServer
public class TransationApplication {

    public static void main(String[] args) {
        SpringApplication.run(TransationApplication.class,args);
    }

}
