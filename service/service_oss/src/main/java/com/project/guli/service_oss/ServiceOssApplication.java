package com.project.guli.service_oss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author wan
 * @create 2020-07-13-15:50
 */
//取消数据源自动配置（由于在service下的pom文件中加入了mysql依赖，具有传递性）
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan({"com.project.guli"})
@EnableDiscoveryClient
public class ServiceOssApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceOssApplication.class, args);
    }

}
