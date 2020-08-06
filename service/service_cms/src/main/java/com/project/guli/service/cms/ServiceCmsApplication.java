package com.project.guli.service.cms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author wan
 * @create 2020-07-25-21:42
 */
@SpringBootApplication
@ComponentScan({"com.project.guli"}) //公共前缀 扫描所有该前缀下的包
@EnableDiscoveryClient
@EnableFeignClients //进行调用时加入即可
public class ServiceCmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceCmsApplication.class,args);
    }
}
