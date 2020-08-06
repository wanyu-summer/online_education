package com.project.guli.service.trade.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.project.guli.common.base.result.ResultCodeEnum;
import com.project.guli.common.base.util.JwtInfo;
import com.project.guli.service.base.dto.CourseDto;
import com.project.guli.service.base.dto.MemberDto;
import com.project.guli.service.base.exception.GuliException;
import com.project.guli.service.trade.entity.Order;
import com.project.guli.service.trade.entity.PayLog;
import com.project.guli.service.trade.feign.EduCourseService;
import com.project.guli.service.trade.feign.UcenterMemberService;
import com.project.guli.service.trade.mapper.OrderMapper;
import com.project.guli.service.trade.mapper.PayLogMapper;
import com.project.guli.service.trade.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.guli.service.trade.util.OrderNoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Payload;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author wwwy
 * @since 2020-07-29
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private EduCourseService eduCourseService;
    @Autowired
    private UcenterMemberService ucenterMemberService;
    @Autowired
    private PayLogMapper payLogMapper;

    @Transactional(rollbackFor = Exception.class)

    @Override
    public String saveOrder(String courseId, String memberId) {

        //查询用户是否已有当前课程的订单
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("course_id", courseId);
        orderQueryWrapper.eq("member_id", memberId);
        Order orderExist = baseMapper.selectOne(orderQueryWrapper);

        //如果订单已存在直接返回订单的id
        if (orderExist != null) {
            return orderExist.getId();
        }

        //远程调用edu UCenter微服务，根据courseId获取courseDto信息，memberId获取memberDto信息
        CourseDto courseDto = eduCourseService.getCourseDtoById(courseId);
        if (courseDto == null) {
            //调用失败，或课程不存在
            throw new GuliException(ResultCodeEnum.PARAM_ERROR);
        }
        MemberDto memberDto = ucenterMemberService.getMemberDtoById(memberId);
        if (memberDto == null) {
            throw new GuliException(ResultCodeEnum.PARAM_ERROR);
        }
        //创建订单
        Order order = new Order();
        order.setOrderNo(OrderNoUtils.getOrderNo());//订单流水号

        //进行赋值
        order.setCourseId(courseId);
        order.setCourseCover(courseDto.getCover());
        order.setCourseTitle(courseDto.getTitle());
        order.setTeacherName(courseDto.getTeacherName());
        order.setTotalFee(courseDto.getPrice().multiply(new BigDecimal(100)));//此处以分为单位

        order.setMemberId(memberId);
        order.setNickname(memberDto.getNickname());
        order.setMobile(memberDto.getMobile());

        order.setStatus(0);
        order.setPayType(1);
        baseMapper.insert(order);
        return order.getId();
    }

    @Override
    public Order getByOrderId(String orderId, String memberId) {

        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("id", orderId);
        orderQueryWrapper.eq("member_id", memberId);
        Order order = baseMapper.selectOne(orderQueryWrapper);
        if (order == null) {
            throw new GuliException(ResultCodeEnum.ORDER_EXIST_ERROR);
        }
        return order;
    }

    @Override
    public Boolean isBuyByCourseId(String courseId, String memberId) {

        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("course_id", courseId);
        orderQueryWrapper.eq("member_id", memberId);
        Order order = baseMapper.selectOne(orderQueryWrapper);
        Integer count = order.getStatus();

        //status为1 已购买 返回true
        return count.intValue() > 0;
    }

    @Override
    public List<Order> selectByMemberId(String memberId) {

        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.orderByDesc("gmt_create");
        orderQueryWrapper.eq("member_id", memberId);

        return baseMapper.selectList(orderQueryWrapper);
    }

    @Override
    public boolean removeById(String orderId, String memberId) {

        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("id", orderId);
        orderQueryWrapper.eq("member_id", memberId);

        return this.remove(orderQueryWrapper);
    }

    @Override
    public Order getOrderByOrderNo(String orderNo) {

        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("order_no", orderNo);
        return baseMapper.selectOne(orderQueryWrapper);
    }

    @Override
    public void updateOrderStatus(Map<String, String> notifyMap) {

        //更新订单状态
        //根据notifyMap获取order
        String orderNo = notifyMap.get("out_trade_code");
        Order order = this.getOrderByOrderNo(orderNo);
        order.setStatus(1);//支付成功
        baseMapper.insert(order);//更新数据库状态

        //更新支付日志
        PayLog payLog = new PayLog();
        payLog.setOrderNo(orderNo);
        payLog.setPayTime(new Date());
        payLog.setPayType(1);//微信支付
        payLog.setTotalFee(order.getTotalFee().longValue());
        payLog.setTradeState(notifyMap.get("result_code"));
        payLog.setTransactionId(notifyMap.get("transaction_id"));
        payLog.setAttr(new Gson().toJson(notifyMap));

        payLogMapper.insert(payLog);

        //更新销量
        eduCourseService.updateBuyCountById(order.getCourseId());
    }

    @Override
    public boolean queryOrderStatus(String orderNo) {

        Order order = this.getOrderByOrderNo(orderNo);
        return order.getStatus() == 1;//为1时支付成功，返回true

    }
}
