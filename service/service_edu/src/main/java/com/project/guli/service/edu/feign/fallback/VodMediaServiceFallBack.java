package com.project.guli.service.edu.feign.fallback;

import com.project.guli.common.base.result.R;
import com.project.guli.service.edu.feign.VodMediaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wan
 * @create 2020-07-24-16:15
 */
@Service
@Slf4j
public class VodMediaServiceFallBack implements VodMediaService {
    @Override
    public R removeVideo(String vodId) {
        log.info("熔断保护");
        return R.error();
    }

    @Override
    public R removeByIdList(List<String> videoIdList) {
        log.info("熔断保护");
        return R.error();
    }
//    @Override
//    public R removeVideo() {
//        return R.error();
//    }
//
//    @Override
//    public R removeByIdList(String url) {
//        log.info("熔断保护");
//        return R.error();
}
