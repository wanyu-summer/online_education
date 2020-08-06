package com.project.guli.service.cms.controller.admin;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.guli.common.base.result.R;
import com.project.guli.service.cms.entity.AdType;
import com.project.guli.service.cms.service.AdTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 推荐位 前端控制器
 * </p>
 *
 * @author wwwy
 * @since 2020-07-26
 */
//@CrossOrigin
@Api(tags = {"推荐位管理"})
@RestController
@RequestMapping("/admin/cms/ad-type")
@Slf4j
public class AdTypeController {

    @Autowired
    private AdTypeService adTypeService;

    @ApiOperation("所有推荐类别列表")
    @GetMapping("list")
    public R listAll() {

        List<AdType> list = adTypeService.list();
        return R.ok().data("items", list);
    }

    @ApiOperation("推荐类别分页列表")
    @GetMapping("list/{page}/{limit}")
    public R listPage(@ApiParam(value = "当前页码", required = true) @PathVariable Long page,
                      @ApiParam(value = "每页记录数", required = true) @PathVariable Long limit) {

        Page<AdType> pageParam = new Page<>(page, limit);
        Page<AdType> pageModel = adTypeService.page(pageParam);
        List<AdType> records = pageModel.getRecords();
        long total = pageModel.getTotal();
        return R.ok().data("total", total).data("rows", records);
    }

    @ApiOperation("根据id删除推荐类别")
    @DeleteMapping("remove/{id}")
    public R removeById(
            @ApiParam(value = "类别id", required = true)
            @PathVariable String id) {

        boolean result = adTypeService.removeById(id);
        if (result) {
            return R.ok().message("删除成功");
        } else {
            return R.error().message("数据不存在");
        }
    }

    @ApiParam("根据ID获取推荐类别")
    @GetMapping("get/{id}")
    public R getById(
            @ApiParam(value = "类别id",required = true)
            @PathVariable String id){

        AdType adType = adTypeService.getById(id);
        if(adType!=null){
            return R.ok().data("item", adType);
        }else {
            return R.error().message("数据不存在");
        }
    }

    @ApiOperation("新增推荐类别")
    @PostMapping("save")
    public R save(
            @ApiParam(value = "推荐类别对象", required = true)
            @RequestBody AdType adType) {

        boolean result = adTypeService.save(adType);
        if (result) {
            return R.ok().message("保存成功");
        } else {
            return R.error().message("保存失败");
        }
    }

    @ApiOperation("更新推荐类别")
    @PutMapping("update")
    public R updateById(
            @ApiParam(value = "推荐类别对象",required = true)
            @RequestBody AdType adType){

        boolean result = adTypeService.updateById(adType);
        if(result){
            return R.ok().message("更新成功");
        }else {
            return R.error().message("数据不存在");
        }
    }
}

