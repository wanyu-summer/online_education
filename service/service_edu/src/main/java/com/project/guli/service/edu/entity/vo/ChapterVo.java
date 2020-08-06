package com.project.guli.service.edu.entity.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wan
 * @create 2020-07-22-10:30
 */
@Data
public class ChapterVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;//章节id
    private String title;//章节标题
    private Integer sort;//章节排序

    private List<VideoVo> children = new ArrayList<>();//子章节
}
