package com.project.guli.service.trade.feign.fallback;

import com.project.guli.common.base.result.R;
import com.project.guli.service.base.dto.CourseDto;
import com.project.guli.service.base.dto.MemberDto;
import com.project.guli.service.trade.feign.EduCourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author wan
 * @create 2020-07-29-20:02
 */
@Slf4j
@Service
public class EduCourseServiceFallback implements EduCourseService {

    @Override
    public CourseDto getCourseDtoById(String courseId) {
        log.info("熔断保护");
        return null;
    }

    @Override
    public R updateBuyCountById(String id) {
        log.info("熔断保护");

        return R.error();
    }
}
