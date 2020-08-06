package com.project.guli.service.edu.entity.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wan
 * @create 2020-07-21-16:37
 */
@Data
public class CoursePublishVo implements Serializable {
    private static final long serialVersionUID = 1L;
    //展示的内容,只用于发布时的显示
    private String id;//课程id
    private String title;//课程标题
    private String subjectParentTitle;//一级分类标题
    private String subjectTitle;//二级分类标题
    private String teacherName;//讲师姓名
    private Integer lessonNum;//课时数
    private String price;//价格
    private String cover;//封面

}
