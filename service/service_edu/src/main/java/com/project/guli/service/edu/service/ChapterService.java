package com.project.guli.service.edu.service;

import com.project.guli.service.edu.entity.Chapter;
import com.baomidou.mybatisplus.extension.service.IService;
import com.project.guli.service.edu.entity.vo.ChapterVo;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author wwwy
 * @since 2020-06-21
 */
public interface ChapterService extends IService<Chapter> {

    boolean removeChaperById(String id);

    List<ChapterVo> nestedList(String courseId);
}
