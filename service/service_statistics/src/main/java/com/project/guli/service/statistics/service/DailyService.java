package com.project.guli.service.statistics.service;

import com.project.guli.service.statistics.entity.Daily;
import com.baomidou.mybatisplus.extension.service.IService;
import com.project.guli.service.statistics.feign.UcenterMemberService;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.PrivateKey;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务类
 * </p>
 *
 * @author wwwy
 * @since 2020-08-01
 */

public interface DailyService extends IService<Daily> {

    //根据日期创建统计数据
    void createStatisticsByDay(String day);

    Map<String, Map<String, Object>> getChartData(String begin, String end);

}
