package com.project.guli.service.edu.entity.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 基于课时的videoVo
 * @author wan
 * @create 2020-07-22-10:25
 */
@Data
public class VideoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String title;
    private Boolean free;
    private Integer sort;

    private String videoSourceId;
}
