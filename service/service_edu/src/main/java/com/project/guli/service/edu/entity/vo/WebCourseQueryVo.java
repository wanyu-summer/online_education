package com.project.guli.service.edu.entity.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 前端网站页面课程显示
 *
 * @author wan
 * @create 2020-07-25-12:57
 */
@Data
public class WebCourseQueryVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String subjectParentId;
    private String subjectId;
    private String buyCountSort;
    private String gmtCreateSort;
    private String priceSort;
    private Integer type;
}
