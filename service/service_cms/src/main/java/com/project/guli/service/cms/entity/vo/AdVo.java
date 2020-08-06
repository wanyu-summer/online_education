package com.project.guli.service.cms.entity.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 查询对象存在多表查询
 * 单一的表满足不了查询内容，因此重新创建查询对象
 *
 * @author wan
 * @create 2020-07-26-20:57
 */
@Data
public class AdVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;//广告id
    private String title;//广告标题
    private String sort;//广告排序
    private String type;//广告位
}
