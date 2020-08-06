package com.project.guli.service.edu.controller.api;

import com.project.guli.common.base.result.R;
import com.project.guli.service.edu.entity.Course;
import com.project.guli.service.edu.entity.Teacher;
import com.project.guli.service.edu.service.CourseService;
import com.project.guli.service.edu.service.TeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author wan
 * @create 2020-07-26-23:14
 */
//@CrossOrigin //解决跨域问题
@Api(tags = {"首页"})
@RestController
@RequestMapping("/api/edu/index")
@Slf4j
public class ApiIndexController {

    @Autowired
    private CourseService courseService;
    @Autowired
    private TeacherService teacherService;

    @ApiOperation("课程列表")
    @GetMapping
    public R index(){
        //获取首页最热门前8个课程数据
        List<Course> courseList=courseService.selectHotCourse();

        //获取首页最热门前4个教师
        List<Teacher> teacherList=teacherService.selectHotTeacher();

        return R.ok().data("courseList", courseList).data("teacherList", teacherList);
    }
}
