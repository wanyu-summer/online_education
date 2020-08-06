package com.project.guli.service.edu.entity.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wan
 * @create 2020-07-21-10:30
 */
@Data
public class CourseVo implements Serializable {

    private static final long serialVersionUID = 1L;
    //展示的内容
    private String id;//课程id
    private String title;//课程标题
    private String subjectParentTitle;//一级分类标题
    private String subjectTitle;//二级分类标题
    private String teacherName;//讲师姓名
    private Integer lessonNum;//课时数
    private String price;//价格
    private String cover;//封面
    private Long buyCount;//购买数量
    private Long viewCount;//浏览数量
    private String status;//状态
    private String gmtCreate;//创建时间

}
