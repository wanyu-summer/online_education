package com.project.guli.service.cms.feign;

import com.project.guli.common.base.result.R;
import com.project.guli.service.cms.feign.fallback.OssFileServiceFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author wan
 * @create 2020-07-14-16:59
 */
//通过注册的微服务名称以及API绝对路径，就可以进行远程调用方法
@Service
@FeignClient(value="service-oss",fallback = OssFileServiceFallBack.class)
public interface OssFileService {
    @GetMapping("/admin/oss/file/test")
    R test();

    @DeleteMapping("/admin/oss/file/remove")
    R removeFile(@RequestBody String url);
}
