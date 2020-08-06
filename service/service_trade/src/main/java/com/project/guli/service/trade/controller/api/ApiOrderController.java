package com.project.guli.service.trade.controller.api;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.project.guli.common.base.result.R;
import com.project.guli.common.base.result.ResultCodeEnum;
import com.project.guli.common.base.util.JwtInfo;
import com.project.guli.common.base.util.JwtUtils;
import com.project.guli.service.trade.entity.Order;
import com.project.guli.service.trade.service.OrderService;
import com.sun.xml.internal.ws.resources.HttpserverMessages;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.List;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author wwwy
 * @since 2020-07-29
 */
@RestController
@RequestMapping("/api/trade/order")
@Api(tags = {"网站订单管理"})
//@CrossOrigin
@Slf4j
public class ApiOrderController {

    @Autowired
    private OrderService orderService;

    @ApiOperation("新增订单")
    @PostMapping("auth/save/{courseId}")  //只有用户登录时才能下订单
    public R save(
            @ApiParam(value = "课程id", required = true)
            @PathVariable String courseId, HttpServletRequest request) {

        JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);
        //从jwtInfo中获取用户id
        String memberId = jwtInfo.getId();
        //创建订单，并得到一个订单号
        String orderId = orderService.saveOrder(courseId, memberId);

        return R.ok().message("订单创建成功").data("orderId", orderId);
    }

    //在前端点击立即购买，生成订单后，根据订单id向后台查询 课程内容进行展示
    @ApiOperation("根据订单id获取订单详情")
    @GetMapping("auth/get/{orderId}")
    public R get(@ApiParam(value = "订单id", required = true) @PathVariable String orderId, HttpServletRequest request) {

        //同样需要在用户登录时查看订单
        JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);
        String memberId = jwtInfo.getId();
        Order order = orderService.getByOrderId(orderId, memberId);
        //不需要判断不存在的情况，当不存在时在实现类就已经抛出订单不存在的异常

        return R.ok().data("item", order);
    }

    @ApiOperation("判断课程是否购买")
    @GetMapping("auth/is-buy/{courseId}")
    public R idBuyByCourseId(@ApiParam(value = "课程id", required = true) @PathVariable String courseId,
                             HttpServletRequest request) {

        JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);
        String memberId = jwtInfo.getId();
        Boolean result = orderService.isBuyByCourseId(courseId, memberId);
        return R.ok().data("isBuy", result);
    }

    @ApiOperation("获取当前用户订单列表")
    @GetMapping("auth/list")
    public R list(HttpServletRequest request) {

        JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);

        List<Order> list = orderService.selectByMemberId(jwtInfo.getId());
        return R.ok().data("items", list);
    }

    @ApiOperation("根据订单id删除订单")
    @DeleteMapping("auth/remove/{orderId}")
    public R remove(@PathVariable String orderId, HttpServletRequest request) {

        JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);
        boolean result = orderService.removeById(orderId, jwtInfo.getId());
        if (result) {
            return R.ok().message("删除成功");
        } else {
            return R.error().message("订单不存在");
        }
    }

    @ApiOperation("查询订单状态")
    @GetMapping("query-pay-status/{orderNo}")
    public R queryPayStatus(@PathVariable String orderNo) {
        boolean result = orderService.queryOrderStatus(orderNo);
        if (result) {
            return R.ok().message("支付成功");
        }
        return R.setResult(ResultCodeEnum.PAY_RUN);//支付中
    }

}

