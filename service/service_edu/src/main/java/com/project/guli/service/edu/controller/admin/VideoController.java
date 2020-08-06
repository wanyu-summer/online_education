package com.project.guli.service.edu.controller.admin;


import com.project.guli.common.base.result.R;
import com.project.guli.service.edu.entity.Video;
import com.project.guli.service.edu.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author wwwy
 * @since 2020-06-21
 */
//@CrossOrigin
@Api(tags = {"课时管理"})
@RestController
@RequestMapping("/admin/edu/video")
@Slf4j
public class VideoController {

    @Autowired
    private VideoService videoService;

    @ApiOperation("新增课时")
    @PostMapping("save")
    public R save(
            @ApiParam(value = "课时对象", required = true)
            @RequestBody Video video) {

        boolean result = videoService.save(video);
        if (result) {
            return R.ok().message("保存成功");
        } else {
            return R.error().message("保存失败");
        }
    }

    @ApiOperation("根据ID查询课时")
    @GetMapping("get/{id}")
    public R getById(
            @ApiParam(value = "课时id", required = true)
            @PathVariable String id) {
        Video video = videoService.getById(id);

        if (video != null) {
            return R.ok().data("item", video);
        } else {
            return R.error().message("数据不存在");
        }
    }

    @ApiOperation("更新课时")
    @PutMapping("update")
    public R updateById(
            @ApiParam(value = "课时对象",required = true)
            @RequestBody Video video){
        boolean result = videoService.updateById(video);
        if(result){
            return R.ok().message("课时更新成功");
        }else{
            return R.error().message("课时不存在");
        }
    }

    @ApiOperation("删除课时")
    @DeleteMapping("remove/{id}")
    public R removeById(
            @ApiParam(value = "课时ID",required = true)
            @PathVariable String id){
        log.warn("VideoController: video id=" + id);

        //删除视频
        //调用删除VOD中的删除视频文件的接口
        videoService.removeMediaVideoById(id);

        boolean result=videoService.removeById(id);
        if(result){
            return R.ok().message("课时删除成功");
        }else{
            return R.error().message("课时删除失败");
        }
    }
}

