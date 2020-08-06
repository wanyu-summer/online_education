package com.project.guli.service.trade.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

/**
 * @author wan
 * @create 2020-07-30-9:54
 */
@Data
@Component
@ConfigurationProperties(prefix = "weixin.pay")
public class WeiXinPayProperties {

    private String appId;
    private String partner;
    private String partnerKey;
    private String notifyUrl;
}
