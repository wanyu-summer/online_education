package com.project.guli.service.edu.controller.admin;

import com.project.guli.common.base.result.R;
import com.project.guli.common.base.result.ResultCodeEnum;
import com.project.guli.common.base.util.ExceptionUtils;
import com.project.guli.service.base.exception.GuliException;
import com.project.guli.service.base.handler.GlobalExceptionHandler;
import com.project.guli.service.edu.entity.vo.SubjectVo;
import com.project.guli.service.edu.service.SubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 课程科目 前端控制器
 *
 * @author wan
 * @create 2020-07-16-16:30
 */
//@CrossOrigin//允许跨域
@Api(tags = {"课程分类管理"})
@RestController
@RequestMapping("admin/edu/subject")
@Slf4j
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    //将数据post到服务器
    @ApiOperation("Excel批量导入")
    @PostMapping("import")
    public R batchImport(
            @ApiParam(value = "Excel文件", required = true)
            @RequestParam("file") MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            subjectService.batchImport(inputStream);
            return R.ok().message("批量导入成功");
        } catch (Exception e) {
            log.error(ExceptionUtils.getMessage(e));
            throw new GuliException(ResultCodeEnum.EXCEL_DATA_IMPORT_ERROR);//自定义的枚举异常
        }
    }

    @ApiOperation("嵌套数据列表")
    @GetMapping("nested-list")
    public R nestedList(){
        List<SubjectVo> subjectVoList = subjectService.nestedList();
        return R.ok().data("items", subjectVoList);

    }
}
