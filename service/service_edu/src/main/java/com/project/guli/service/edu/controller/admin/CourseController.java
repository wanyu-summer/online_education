package com.project.guli.service.edu.controller.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.project.guli.common.base.result.R;
import com.project.guli.service.edu.entity.Video;
import com.project.guli.service.edu.entity.form.CourseInfoForm;
import com.project.guli.service.edu.entity.vo.CoursePublishVo;
import com.project.guli.service.edu.entity.vo.CourseQueryVo;
import com.project.guli.service.edu.entity.vo.CourseVo;
import com.project.guli.service.edu.service.CourseService;
import com.project.guli.service.edu.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author wwwy
 * @since 2020-06-21
 */
//@CrossOrigin//允许跨域
@Api(tags = {"课程管理"})
@RestController
@RequestMapping("/admin/edu/course")
@Slf4j
public class CourseController {


    @Autowired
    private CourseService courseService;

    @Autowired
    private VideoService videoService;

    @ApiOperation("新增课程")
    @PostMapping("save-course-info")
    public R saveCourseInfo(
            @ApiParam(value = "课程信息", required = true)
            @RequestBody CourseInfoForm courseInfoForm
    ) {
        String courseId = courseService.saveCourseInfo(courseInfoForm);//此时应该得到课程id，将课程id传给前端
        return R.ok().data("courseId", courseId).message("保存成功");
    }

    @ApiOperation("根据ID查询课程")
    @GetMapping("course-info/{id}")
    public R getById(
            @ApiParam(value = "课程ID", required = true)
            @PathVariable String id
    ) {
        CourseInfoForm courseInfoForm = courseService.getCourseInfoById(id);
        if (courseInfoForm != null) {
            return R.ok().data("item", courseInfoForm);
        } else {
            return R.ok().message("数据不存在");
        }
    }

    @ApiOperation("更新课程")
    @PutMapping("update-course-info")
    public R updateById(
            @ApiParam(value = "课程ID", required = true)
            @RequestBody CourseInfoForm courseInfoForm) {

        courseService.updateCourseInfoById(courseInfoForm);

        return R.ok().message("更新成功");
    }

    @ApiOperation("课程分页列表")
    @GetMapping("list/{page}/{limit}")
    public R listPage(@ApiParam(value = "当前页码", required = true) @PathVariable Long page,
                      @ApiParam(value = "每页记录数", required = true) @PathVariable Long limit,
                      @ApiParam("课程列表查询对象") CourseQueryVo courseQueryVo) {

//        Page<Course> pageParam = new Page<>(page, limit);
        IPage<CourseVo> courseVoPage = courseService.selectPage(page, limit, courseQueryVo);
        List<CourseVo> records = courseVoPage.getRecords();
        long total = courseVoPage.getTotal();//总记录数
        return R.ok().data("total", total).data("rows", records);
    }

    @ApiOperation(value = "根据ID删除课程")
    @DeleteMapping("remove/{id}")
    public R removeById(
            @ApiParam(value = "课程ID", required = true)
            @PathVariable String id) {

        //删除课程视频
        videoService.removeVideoByCourseId(id);
        //删除课程封面
        courseService.removeCoverById(id);

        //删除课程
        boolean result = courseService.removeCourseById(id);
        if (result) {
            return R.ok().message("删除成功");
        } else {
            return R.ok().message("数据不存在");
        }
    }

    @ApiOperation("根据ID获取课程发布信息")
    @GetMapping("course-publish/{id}")
    public R getCoursePublishVoById(
            @ApiParam(value = "课程ID", required = true)
            @PathVariable String id) {
        CoursePublishVo coursePublishVo = courseService.getCoursePublishById(id);
        if (coursePublishVo != null) {
            return R.ok().data("item", coursePublishVo);
        } else {
            return R.ok().message("数据不存在");
        }
    }

    @ApiOperation("根据ID发布课程")
    @PutMapping("publish-course/{id}")
    public R publishCourseById(
            @ApiParam(value = "课程ID", required = true)
            @PathVariable String id) {
        boolean result = courseService.publishCourseById(id);
        if (result) {
            return R.ok().message("课程发布成功");
        }else {
            return R.ok().message("课程不存在");
        }
    }
}

