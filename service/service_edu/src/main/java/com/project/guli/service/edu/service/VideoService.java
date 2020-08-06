package com.project.guli.service.edu.service;

import com.project.guli.service.edu.entity.Video;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author wwwy
 * @since 2020-06-21
 */
public interface VideoService extends IService<Video> {
    void removeVideoByCourseId(String id);

    void removeMediaVideoById(String id);

    void removeMediaVideoByChapterId(String id);


//    boolean removeVideoById(String id);
}
