package com.project.guli.service.edu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author wan
 * @create 2020-06-21-22:47
 */
@SpringBootApplication
@ComponentScan({"com.project.guli"}) //公共前缀 扫描所有该前缀下的包
@EnableDiscoveryClient
@EnableFeignClients //进行调用时加入即可
@EnableTransactionManagement
public class ServiceEduApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceEduApplication.class,args);
    }
}
