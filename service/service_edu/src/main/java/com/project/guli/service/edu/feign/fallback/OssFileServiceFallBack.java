package com.project.guli.service.edu.feign.fallback;

import com.project.guli.common.base.result.R;
import com.project.guli.service.edu.feign.OssFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 当远程调用oss服务失败，启动备用方案，这里返回一个简单的信息
 * 同时告诉OssFileService当远程调用不了，可以启动备选方案
 * @author wan
 * @create 2020-07-14-21:55
 */
@Service
@Slf4j
public class OssFileServiceFallBack implements OssFileService {
    @Override
    public R test() {
        return R.error();
    }

    @Override
    public R removeFile(String url) {
        log.info("熔断保护");
        return R.error();
    }
}
