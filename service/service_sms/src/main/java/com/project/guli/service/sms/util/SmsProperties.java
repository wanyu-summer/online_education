package com.project.guli.service.sms.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 从配置文件中读取常量
 *
 * @author wan
 * @create 2020-07-28-16:43
 */
@Data
@Component
@ConfigurationProperties(prefix = "aliyun.sms")
public class SmsProperties {

    private String regionId;
    private String keyId;
    private String keySecret;
    private String templateCode;
    private String signName;

}
