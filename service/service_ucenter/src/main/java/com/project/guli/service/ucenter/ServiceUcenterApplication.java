package com.project.guli.service.ucenter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

import javax.xml.ws.Service;

/**
 * @author wan
 * @create 2020-07-28-19:34
 */
@SpringBootApplication
@ComponentScan({"com.project.guli"})
@EnableDiscoveryClient
public class ServiceUcenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceUcenterApplication.class, args);
    }


}
