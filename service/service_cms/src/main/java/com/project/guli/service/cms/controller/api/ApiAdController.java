package com.project.guli.service.cms.controller.api;

import com.project.guli.common.base.result.R;
import com.project.guli.service.cms.entity.Ad;
import com.project.guli.service.cms.service.AdService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author wan
 * @create 2020-07-26-22:30
 */
//@CrossOrigin //解决跨域问题
@Api(tags = {"广告推荐"})
@RestController
@RequestMapping("/api/cms/ad")
@Slf4j
public class ApiAdController {

    @Autowired
    private AdService adService;

    @Autowired
    private RedisTemplate redisTemplate;

    @ApiOperation("根据推荐位ID显示广告推荐列表")
    @GetMapping("list/{adTypeId}")
    public R listByAdTypeId(@ApiParam(value = "推荐位ID", required = true)
                            @PathVariable String adTypeId) {
        List<Ad> ads = adService.selectByAdTypeId(adTypeId);

        return R.ok().data("items", ads);
    }

    @PostMapping("save-test")
    public R saveAd(@RequestBody Ad ad){
        redisTemplate.opsForValue().set("ad", ad);
        return R.ok();
    }

}
