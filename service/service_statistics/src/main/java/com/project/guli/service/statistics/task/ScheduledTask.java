package com.project.guli.service.statistics.task;

import com.project.guli.service.statistics.service.DailyService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author wan
 * @create 2020-08-02-17:41
 */
@Component
@Slf4j
public class ScheduledTask {

    //需求：每天凌晨1点自动生成前一天的统计数据
    @Autowired
    private DailyService dailyService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void testGenStatisticsData(){
        //得到今天的日期，根据今天的日期计算昨天的日期
        String day = new DateTime().minusDays(1).toString("yyyy-MM-dd");

        dailyService.createStatisticsByDay(day);
    }
}
