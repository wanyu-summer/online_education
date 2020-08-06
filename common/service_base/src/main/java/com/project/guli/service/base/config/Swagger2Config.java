package com.project.guli.service.base.config;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author wan
 * @create 2020-06-22-0:05
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

    //用户看到的网页
    @Bean
    public Docket webApiConfig(){
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("webApi")
                .apiInfo(webApiInfo())
                .select()
                .paths(Predicates.and(PathSelectors.regex("/api/.*")))
                .build();
    }

    //后台管理员控制的网页
    @Bean
    public Docket adminApiConfig(){
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("adminApi")
                .apiInfo(adminApiInfo())
                .select()
                .paths(Predicates.and(PathSelectors.regex("/admin/.*")))
                .build();
    }

    //web网页辅助方法
    private ApiInfo webApiInfo(){
        return new ApiInfoBuilder()
                .title("网站的API文档")
                .description("本文档描述了谷粒学院网站的API接口定义")
                .version("1.0")
                .contact(new Contact("wy","http://wy.com","947962697@qq.com"))
                .build();
    }

    //admin后台网页辅助方法
    private ApiInfo adminApiInfo(){
        return new ApiInfoBuilder()
                .title("后台管理系统的API文档")
                .description("本文档描述了谷粒学院后台管理系统的API接口定义")
                .version("1.0")
                .contact(new Contact("wy","http://wy.com","947962697@qq.com"))
                .build();
    }
}
