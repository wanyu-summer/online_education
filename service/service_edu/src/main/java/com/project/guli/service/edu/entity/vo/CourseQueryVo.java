package com.project.guli.service.edu.entity.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wan
 * @create 2020-07-21-10:22
 */
@Data
public class CourseQueryVo implements Serializable {
    private static final long serialVersionUID = 1L;

    //对应查询类别
    private String title;
    private String teacherId;
    private String subjectParentId;
    private String subjectId;

}
