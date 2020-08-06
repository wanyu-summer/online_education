package com.project.guli.service.vod.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 从yml中读取key字段，解耦合，相当于先将字段存入容器
 *
 * @author wan
 * @create 2020-07-22-17:26
 */
@Data
@Component
@ConfigurationProperties(prefix = "aliyun.vod")
public class VodProperties {
    private String keyid;
    private String keysecret;
    private String templateGroupId;
    private String workflowId;
}
