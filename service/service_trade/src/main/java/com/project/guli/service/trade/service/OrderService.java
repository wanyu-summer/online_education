package com.project.guli.service.trade.service;

import com.project.guli.common.base.util.JwtInfo;
import com.project.guli.service.trade.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.cloud.openfeign.FeignClient;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author wwwy
 * @since 2020-07-29
 */

public interface OrderService extends IService<Order> {

    String saveOrder(String courseId, String memberId);

    Order getByOrderId(String orderId, String memberId);

    Boolean isBuyByCourseId(String courseId, String memberId);

    List<Order> selectByMemberId(String memberId);

    boolean removeById(String orderId, String memberId);

    //根据订单流水号查询订单
    Order getOrderByOrderNo(String orderNo);

    void updateOrderStatus(Map<String, String> notifyMap);

    boolean queryOrderStatus(String orderNo);
}
