package com.project.guli.service.trade.feign;

import com.project.guli.common.base.result.R;
import com.project.guli.service.base.dto.CourseDto;
import com.project.guli.service.trade.feign.fallback.EduCourseServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 远程调用edu服务与UCenter服务
 *
 * @author wan
 * @create 2020-07-29-19:17
 */
@Service
@FeignClient(value = "service-edu", fallback = EduCourseServiceFallback.class)
public interface EduCourseService {

    @GetMapping("/api/edu/course/inner/get-course-dto/{courseId}")
    CourseDto getCourseDtoById(@PathVariable(value = "courseId") String courseId);

    @GetMapping("/api/edu/course/inner/update-buy-count/{id}")
    R updateBuyCountById(@PathVariable("id") String id);
}
