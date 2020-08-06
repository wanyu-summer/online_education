package com.project.guli.service.vod.controller.admin;

import com.project.guli.common.base.result.R;
import com.project.guli.common.base.result.ResultCodeEnum;
import com.project.guli.common.base.util.ExceptionUtils;
import com.project.guli.service.base.exception.GuliException;
import com.project.guli.service.vod.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author wan
 * @create 2020-07-22-20:42
 */
//@CrossOrigin
@Api(tags = {"阿里云视频点播"})
@RestController
@RequestMapping("/admin/vod/media")
@Slf4j
public class MediaController {

    @Autowired
    private VideoService videoService;

    @ApiOperation("视频上传")
    @PutMapping("upload")
    public R uploadVideo(
            @ApiParam(value = "文件", required = true)
            @RequestParam("file") MultipartFile file) {

        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
            String originalFilename = file.getOriginalFilename();
            String videoId = videoService.uploadVideo(inputStream, originalFilename);
            return R.ok().message("视频上传成功").data("videoId", videoId);
        } catch (IOException e) {
            log.error(ExceptionUtils.getMessage(e));
            throw new GuliException(ResultCodeEnum.VIDEO_UPLOAD_TOMCAT_ERROR);
        }
    }

    @ApiOperation("根据id删除视频")
    @DeleteMapping("remove/{vodId}")
    public R removeVideo(
            @ApiParam(name = "vodId", value = "阿里云视频id", required = true)
            @PathVariable String vodId) {

        log.warn("service-vod MediaController:videoSourceId=" + vodId);
        try {
            videoService.removeVideo(vodId);
            return R.ok().message("视频删除成功");
        } catch (Exception e) {
            log.error(ExceptionUtils.getMessage(e));
            throw new GuliException(ResultCodeEnum.VIDEO_DELETE_ALIYUN_ERROR);
        }
    }

    @ApiOperation("根据id列表批量删除video")
    @DeleteMapping("remove")
    public R removeByIdList(
            @ApiParam(value = "阿里云视频列表", required = true)
            @RequestBody List<String> videoIdList) {
        try {
            videoService.removeVideoByIdList(videoIdList);
            return  R.ok().message("视频删除成功");
        } catch (Exception e) {
            log.error(ExceptionUtils.getMessage(e));
            throw new GuliException(ResultCodeEnum.VIDEO_DELETE_ALIYUN_ERROR);
        }
    }
}
