package com.project.guli.service.statistics.feign.fallback;

import com.project.guli.common.base.result.R;
import com.project.guli.service.statistics.feign.UcenterMemberService;
import jdk.nashorn.internal.runtime.logging.Logger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author wan
 * @create 2020-08-01-17:54
 */
@Service
@Slf4j
public class UcenterMemberServiceFallback implements UcenterMemberService {

    @Override
    public R countRegisterNum(String day) {
        log.error("熔断保护");
        return R.ok().data("registerNum", 0);//熔断后显示为0
    }
}
