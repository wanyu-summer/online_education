package com.project.guli.service.trade;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author wan
 * @create 2020-07-29-17:18
 */
@SpringBootApplication
@ComponentScan({"com.project.guli"})
@EnableDiscoveryClient //注册到注册中心
@EnableFeignClients //支持远程调用
public class ServiceTradeApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceTradeApplication.class, args);
    }

}
