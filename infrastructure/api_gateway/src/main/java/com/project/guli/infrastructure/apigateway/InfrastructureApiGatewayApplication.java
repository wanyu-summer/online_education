package com.project.guli.infrastructure.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 *
 * 由于在pom中加入gateway依赖，因此就是一个网关项目
 * @author wan
 * @create 2020-07-31-16:30
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableDiscoveryClient //将会被注册到nacos注册中心，springboot2.0以后不需要加
public class InfrastructureApiGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(InfrastructureApiGatewayApplication.class, args);
    }
}
