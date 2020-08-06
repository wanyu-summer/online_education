package com.project.guli.service.edu.mapper;

import com.project.guli.service.base.dto.CourseDto;
import com.project.guli.service.edu.entity.Course;
import com.project.guli.service.edu.entity.vo.CoursePublishVo;
import com.project.guli.service.edu.entity.vo.CourseVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.guli.service.edu.entity.vo.WebCourseQueryVo;
import com.project.guli.service.edu.entity.vo.WebCourseVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author Helen
 * @since 2020-04-01
 */
@Repository
public interface CourseMapper extends BaseMapper<Course> {

    List<CourseVo> selectPageByCourseQueryVo(
            Page<CourseVo> pageParam,
            @Param(Constants.WRAPPER) QueryWrapper<CourseVo> queryWrapper);

    CoursePublishVo selectCoursePublishVoById(String id);

    WebCourseVo selectWebCourseVoById(String id);

    CourseDto selectCourseDtoById(String courseId);
//    CoursePublishVo selectCoursePublishVoById(String id);
}
