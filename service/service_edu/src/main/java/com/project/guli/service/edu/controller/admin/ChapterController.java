package com.project.guli.service.edu.controller.admin;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.project.guli.common.base.result.R;
import com.project.guli.service.edu.entity.Chapter;
import com.project.guli.service.edu.entity.Video;
import com.project.guli.service.edu.entity.vo.ChapterVo;
import com.project.guli.service.edu.feign.VodMediaService;
import com.project.guli.service.edu.mapper.VideoMapper;
import com.project.guli.service.edu.service.ChapterService;
import com.project.guli.service.edu.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
@Api(tags = {"章节管理"})
@RestController
@RequestMapping("/admin/edu/chapter")
@Slf4j
@RefreshScope
public class ChapterController {

    @Autowired
    private ChapterService chapterService;

    @Autowired
    private VideoService videoService;


    @ApiOperation("新增章节")
    @PostMapping("save")
    public R save(
            @ApiParam(value = "章节对象", required = true)
            @RequestBody Chapter chapter) {
        boolean result = chapterService.save(chapter);
        if (result) {
            return R.ok().message("保存成功");
        } else {
            return R.error().message("保存失败");
        }
    }

    //Chapter类中定义的是课程id,但此处id是章节自身id
    @ApiOperation("根据ID查询章节")
    @GetMapping("get/{id}")
    public R getById(
            @ApiParam(value = "章节ID", required = true)
            @PathVariable String id) {

        Chapter chapter = chapterService.getById(id);
        if (chapter != null) {
            //代表存在
            return R.ok().data("item", chapter);
        } else {
            return R.error().message("数据不存在");
        }
    }

    //更新章节传入chapter对象
    @ApiOperation("更新章节")
    @PutMapping("update")
    public R updateById(
            @ApiParam(value = "章节对象", required = true)
            @RequestBody Chapter chapter) {

        boolean result = chapterService.updateById(chapter);
        if (result) {
            return R.ok().message("更新成功");
        } else {
            return R.error().message("数据不存在");
        }
    }

    //根据id删除章节
    @ApiOperation("根据ID删除章节")
    @DeleteMapping("remove/{id}")
    public R removeById(
            @ApiParam(value = "章节ID", required = true)
            @PathVariable String id) {

        //删除课程视频，在课程服务中添加根据chapterid删除视频的方法
        videoService.removeMediaVideoByChapterId(id);

        boolean result = chapterService.removeChaperById(id);
        if (result) {
            return R.ok().message("删除成功");
        } else {
            return R.error().message("数据不存在");
        }
    }

    //根据课程ID显示当前课程下的章节列表
    @ApiOperation("嵌套章节列表")
    @GetMapping("nested-list/{courseId}")
    public R nestedListByCourseId(
        @ApiParam(value = "课程ID",required = true)
        @PathVariable String courseId){

        List<ChapterVo> chapterVoList = chapterService.nestedList(courseId);
        return R.ok().data("items", chapterVoList);
    }
}

