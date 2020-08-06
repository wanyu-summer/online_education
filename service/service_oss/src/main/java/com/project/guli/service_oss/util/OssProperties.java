package com.project.guli.service_oss.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author wan
 * @create 2020-07-13-16:00
 */
@Data
@Component
@ConfigurationProperties(prefix = "aliyun.oss")
//从配置文件读取常量
public class OssProperties {
    private String endPoint;
    private String keyId;
    private String keySecret;
    private String bucketName;

}
