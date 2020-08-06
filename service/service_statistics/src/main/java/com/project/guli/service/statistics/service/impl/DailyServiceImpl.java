package com.project.guli.service.statistics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.project.guli.common.base.result.R;
import com.project.guli.service.statistics.entity.Daily;
import com.project.guli.service.statistics.feign.UcenterMemberService;
import com.project.guli.service.statistics.mapper.DailyMapper;
import com.project.guli.service.statistics.service.DailyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.org.apache.bcel.internal.generic.DALOAD;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author wwwy
 * @since 2020-08-01
 */
@Service
public class DailyServiceImpl extends ServiceImpl<DailyMapper, Daily> implements DailyService {

    @Autowired
    private UcenterMemberService ucenterMemberService;

    //存在数据的删与除，加上事务的注解
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createStatisticsByDay(String day) {

        //先判断数据库中是否已存在，如果存在则删除
        QueryWrapper<Daily> dailyQueryWrapper = new QueryWrapper<>();
        dailyQueryWrapper.eq("date_calculated", day);
        baseMapper.delete(dailyQueryWrapper);

        //远程获取注册用户数的统计结果
        R r = ucenterMemberService.countRegisterNum(day);
        Integer registerNum = (Integer) r.getData().get("registerNum");

        //利用随机数获取其他统计数据
        //每日登陆人数
        int loginNum = RandomUtils.nextInt(100, 200);
        //每日视频观看数
        int videoViewNum = RandomUtils.nextInt(100, 200);
        //每日课程新增数
        int courseNum = RandomUtils.nextInt(100, 200);

        //创建统计数据对象
        Daily daily = new Daily();

        daily.setCourseNum(courseNum);
        daily.setDateCalculated(day);
        daily.setLoginNum(loginNum);
        daily.setRegisterNum(registerNum);
        daily.setVideoViewNum(videoViewNum);

        baseMapper.insert(daily);
    }

    @Override
    public Map<String, Map<String, Object>> getChartData(String begin, String end) {

        //传入开始时间与结束时间，得到相关统计数据

        //学员登录数统计
        Map<String, Object> loginNum = getChartDataByType(begin, end, "login_num");
        //每日新增课程数统计
        Map<String, Object> courseNum = getChartDataByType(begin, end, "course_num");
        //课程播放数
        Map<String, Object> videoViewNum = getChartDataByType(begin, end, "video_view_num");
        //学员注册数
        Map<String, Object> registerNum = getChartDataByType(begin, end, "register_num");
        Map<String, Map<String, Object>> map = new HashMap<>();
        map.put("register_num", registerNum);
        map.put("video_view_num", videoViewNum);
        map.put("course_num", courseNum);
        map.put("login_num", loginNum);
        return map;
    }

    //辅助方法,根据时间和查询的数据类型查询数据
    private Map<String, Object> getChartDataByType(String begin, String end, String type) {
        Map<String, Object> map = new HashMap<>();

        List<String> xList = new ArrayList<>();//日期列表
        List<Integer> yList = new ArrayList<>();//数据列表

        QueryWrapper<Daily> dailyQueryWrapper = new QueryWrapper<>();
        dailyQueryWrapper.select("date_calculated", type);
        dailyQueryWrapper.between("date_calculated", begin, end);

        List<Map<String, Object>> maps = baseMapper.selectMaps(dailyQueryWrapper);
        //Map中string表示日期字段和查询字段名称type，object表示具体日期内容以及具体查询字段内容
        for (Map<String, Object> data : maps) {
            //data中的string对应日期，object对应具体日期
            String dateCalculated = (String) data.get("date_calculated");
            xList.add(dateCalculated);
            Integer dataValue = (Integer) data.get(type);
            yList.add(dataValue);

        }
        //将数据列表放入map中
        map.put("xData", xList);
        map.put("yData", yList);
        return map;
    }
}
