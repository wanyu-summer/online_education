package com.project.guli.service.edu.controller.api;

import com.project.guli.common.base.result.R;
import com.project.guli.service.edu.entity.Subject;
import com.project.guli.service.edu.entity.vo.SubjectVo;
import com.project.guli.service.edu.service.SubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author wan
 * @create 2020-07-25-14:59
 */
@RequestMapping("/api/edu/subject")
//@CrossOrigin
@Api(tags = {"课程分类"})
@Slf4j
@RestController
public class ApiSubjectController {

    @Autowired
    private SubjectService subjectService;

    @ApiOperation("嵌套数据列表")
    @GetMapping("nested-list")
    public R nestedList() {
        List<SubjectVo> subjectVoList = subjectService.nestedList();
        return R.ok().data("items", subjectVoList);
    }
}
