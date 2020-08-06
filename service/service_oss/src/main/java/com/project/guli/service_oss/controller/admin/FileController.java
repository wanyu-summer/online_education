package com.project.guli.service_oss.controller.admin;

import com.project.guli.common.base.result.R;
import com.project.guli.common.base.result.ResultCodeEnum;
import com.project.guli.common.base.util.ExceptionUtils;
import com.project.guli.service.base.exception.GuliException;
import com.project.guli.service_oss.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * @author wan
 * @create 2020-07-13-17:40
 */
@Api(tags = {"阿里云文件管理"})
//@CrossOrigin
@RestController
@RequestMapping("/admin/oss/file")
@Slf4j
public class FileController {

    @Autowired
    private FileService fileService;

    @ApiOperation("文件上传")
    @PostMapping("upload")
    public R upload(
            @ApiParam(value = "文件",required = true)
            @RequestParam("file") MultipartFile file,
            @ApiParam(value = "模块",required = true)
            @RequestParam("module") String module) {

        try {
            InputStream inputStream = file.getInputStream();
            String originalFilename = file.getOriginalFilename();
            //获取url
            String uploadUrl = fileService.upload(inputStream, module, originalFilename);

            return R.ok().message("文件上传成功").data("url",uploadUrl);
        } catch (Exception e) {
            log.error(ExceptionUtils.getMessage(e));
            throw new GuliException(ResultCodeEnum.FILE_UPLOAD_ERROR);
        }
    }

//    业务分库情况下，edu微服务调用对应的数据库，获取url，再将url传给oss服务removeFile方法，此时edu微服务调用oss服务
    @ApiOperation(value = "文件删除")
    @DeleteMapping("remove")
    public R removeFile(
            @ApiParam(value = "要删除的文件url路径",required = true)
            @RequestBody String url){

        fileService.removeFile(url);
        return R.ok().message("文件删除成功");
    }
    //添加一个测试方法
    @ApiOperation(value = "测试")
    @GetMapping("test")
    public R test(){
        log.info("oss test 被调用");
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return R.ok();
    }
}
