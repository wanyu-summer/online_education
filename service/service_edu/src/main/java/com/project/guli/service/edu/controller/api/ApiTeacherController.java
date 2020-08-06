package com.project.guli.service.edu.controller.api;

import com.project.guli.common.base.result.R;
import com.project.guli.service.edu.entity.Teacher;
import com.project.guli.service.edu.service.TeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 基于前端网站的后台接口
 *
 * @author wan
 * @create 2020-07-24-21:45
 */
//@CrossOrigin
@RestController
@Api(tags = {"讲师"})
@RequestMapping("/api/edu/teacher")
public class ApiTeacherController {

    @Autowired
    private TeacherService teacherService;

    @ApiOperation("所有讲师列表")
    @GetMapping("list")
    public R listAll() {
        List<Teacher> list = teacherService.list();
        return R.ok().data("items", list).message("获取讲师列表成功");
    }

    @ApiOperation("根据id获取讲师")
    @GetMapping("get/{id}")
    public R getById(
            @ApiParam(value = "讲师id",required = true)
            @PathVariable String id){
        Map<String, Object> map = teacherService.selectTeacherInfoById(id);
        return R.ok().data(map);
    }

}
