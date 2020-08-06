package com.project.guli.service.statistics.controller.admin;


import com.project.guli.common.base.result.R;
import com.project.guli.service.statistics.service.DailyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author wwwy
 * @since 2020-08-01
 */
@Api(tags = {"统计分析管理"})
@RestController
@RequestMapping("/admin/statistics/daily")
public class DailyController {
    @Autowired
    private DailyService dailyService;

    @ApiOperation("生成统计记录")
    @PostMapping("create/{day}")
    public R createdStatisticsByDay(
            @ApiParam(value = "统计日期")
            @PathVariable String day) {
        dailyService.createStatisticsByDay(day);

        return R.ok().message("统计数据生成成功");
    }

    @ApiOperation("展示统计数据")
    @GetMapping("show-chart/{begin}/{end})")
    public R showChart(@ApiParam(value = "开始时间") @PathVariable String begin,
                       @ApiParam(value = "结束时间") @PathVariable String end) {
        Map<String, Map<String, Object>> map = dailyService.getChartData(begin, end);
        return R.ok().data("chartData", map);
    }
}

