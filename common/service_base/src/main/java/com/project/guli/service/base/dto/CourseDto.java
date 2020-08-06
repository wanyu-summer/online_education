package com.project.guli.service.base.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 封装订单与课程之间的相关数据
 * @author wan
 * @create 2020-07-29-17:33
 */
@Data
public class CourseDto implements Serializable {
    private static final long serialVersionUID=1L;

    private String id;//课程id
    private String title;//课程标题
    private BigDecimal price;//课程销售价格
    private String cover;//课程封面图片路径
    private String teacherName;//课程讲师

}
