package com.project.guli.service.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.project.guli.service.edu.entity.Video;
import com.project.guli.service.edu.feign.VodMediaService;
import com.project.guli.service.edu.mapper.VideoMapper;
import com.project.guli.service.edu.service.VideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author wwwy
 * @since 2020-06-21
 */
@Service
@Slf4j
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements VideoService {

    @Autowired
    private VodMediaService vodMediaService;

    @Override
    public void removeVideoByCourseId(String id) {
        QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>();
        videoQueryWrapper.select("video_source_id");
        videoQueryWrapper.eq("course_id", id);

        List<Map<String, Object>> maps = baseMapper.selectMaps(videoQueryWrapper);
        List<String> videoSourceIdList = getVideoSourceId(maps);
        vodMediaService.removeByIdList(videoSourceIdList);
    }

    @Override
    public void removeMediaVideoById(String id) {
        log.warn("VideoServiceImpl:video id=" + id);
        //根据videoid找到视频id
        Video video = baseMapper.selectById(id);
        String videoSourceId = video.getCourseId();
        log.warn("VideoServiceImpl：video id=" + videoSourceId);
        vodMediaService.removeVideo(videoSourceId);
    }

    @Override
    public void removeMediaVideoByChapterId(String id) {
        //根据chapter_id删除视频
        //根据chapter_id删除对应的video_source_id对应的视频
        QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>();
        videoQueryWrapper.select("video_source_id");
        videoQueryWrapper.eq("chapter_id", id);

        List<Map<String, Object>> maps = baseMapper.selectMaps(videoQueryWrapper);
        List<String> videoSourceIdList = getVideoSourceId(maps);
        vodMediaService.removeByIdList(videoSourceIdList);
    }

    public List<String> getVideoSourceId(List<Map<String, Object>> maps) {
        ArrayList<String> videoSourceIdList = new ArrayList<>();
        //遍历maps将video_source_id添加至列表中
        for (Map<String, Object> map : maps) {
            videoSourceIdList.add((String) (map.get("video_source_id")));
        }
        return videoSourceIdList;
    }
}



