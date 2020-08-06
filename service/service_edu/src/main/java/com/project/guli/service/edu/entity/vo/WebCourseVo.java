package com.project.guli.service.edu.entity.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 进入课程显示详细信息
 *
 * @author wan
 * @create 2020-07-25-19:44
 */
@Data
public class WebCourseVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String title;
    private BigDecimal price;
    private Integer lessonNum;
    private String cover;
    private Long buyCount;
    private Long viewCount;
    private String description;
    private String teacherId;
    private String teacherName;
    private String intro;
    private String avatar;
    private String subjectLevelOneId;
    private String subjectLevelOne;
    private String subjectLevelTwoId;
    private String subjectLevelTwo;
}
