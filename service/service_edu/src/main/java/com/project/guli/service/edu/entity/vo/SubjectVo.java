package com.project.guli.service.edu.entity.vo;

import com.project.guli.service.edu.entity.Subject;
import lombok.Data;
import org.apache.ibatis.javassist.SerialVersionUID;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wan
 * @create 2020-07-16-19:03
 */
@Data
public class SubjectVo {
//    private static final long SerialVersionUID = 1L;

    private String id;
    private String title;
    private Integer sort;

    private List<SubjectVo> children = new ArrayList<>();

}
