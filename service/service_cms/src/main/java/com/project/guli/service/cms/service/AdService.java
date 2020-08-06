package com.project.guli.service.cms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.project.guli.service.cms.entity.Ad;
import com.baomidou.mybatisplus.extension.service.IService;
import com.project.guli.service.cms.entity.vo.AdVo;

import java.util.List;

/**
 * <p>
 * 广告推荐 服务类
 * </p>
 *
 * @author wwwy
 * @since 2020-07-26
 */
public interface AdService extends IService<Ad> {

    boolean removeAdImageById(String id);

    IPage<AdVo> selectPage(Long page, Long limit);

    List<Ad> selectByAdTypeId(String adTypeId);

}
