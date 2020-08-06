package com.project.guli.service.edu.controller.api;

import com.project.guli.common.base.result.R;
import com.project.guli.service.base.dto.CourseDto;
import com.project.guli.service.base.dto.MemberDto;
import com.project.guli.service.edu.entity.Course;
import com.project.guli.service.edu.entity.vo.ChapterVo;
import com.project.guli.service.edu.entity.vo.WebCourseQueryVo;
import com.project.guli.service.edu.entity.vo.WebCourseVo;
import com.project.guli.service.edu.service.ChapterService;
import com.project.guli.service.edu.service.CourseService;
import com.sun.xml.bind.v2.model.core.ID;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.util.List;

/**
 * @author wan
 * @create 2020-07-25-13:38
 */
@RestController
//@CrossOrigin
@Api(tags = {"课程"})
@RequestMapping("/api/edu/course")
@Slf4j
public class ApiCourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private ChapterService chapterService;

    //课程列表查询，传入查询条件，webCourseQueryVo
    @ApiOperation("课程列表展示")
    @GetMapping("list")
    public R pageList(
            @ApiParam(value = "查询对象", required = true)
                    WebCourseQueryVo webCourseQueryVo) {//当加上requestBody时，每个查询条件均要写

        //传入查询对象，返回course列表
        List<Course> courseList = courseService.webSelectList(webCourseQueryVo);
        //将列表返回前端
        return R.ok().data("courseList", courseList);
    }

    @ApiOperation("根据id查询课程详细信息")
    @GetMapping("get/{courseId}")
    public R getById(
            @ApiParam(value = "课程id", required = true)
            @PathVariable String courseId) {

        //查询课程信息和讲师信息
        WebCourseVo webCourseVo = courseService.selectWebCourseVoById(courseId);

        //查询章节信息
        List<ChapterVo> chapterList = chapterService.nestedList(courseId);

        return R.ok().data("course", webCourseVo).data("chapterVoList", chapterList);
    }

    //微服务与微服务之间的数据传输，不需要传给前端
    @ApiOperation("根据课程id查询课程信息")
    @GetMapping("inner/get-course-dto/{courseId}")
    public CourseDto getCourseDtoById(
            @ApiParam(value = "课程id", required = true)
            @PathVariable String courseId) {

        CourseDto courseDto = courseService.getCourseDtoById(courseId);
        return courseDto;
    }

    //trade微服务中远程调用，此时结果会传给前端显示，因此返回R
    @ApiOperation("根据id更新购买数量")
    @GetMapping("inner/update-buy-count/{id}")
    public R updateBuyCountById(
            @ApiParam(value = "课程id", required = true)
            @PathVariable String id) {
        courseService.updateBuyCountById(id);

        return R.ok();
    }
}
