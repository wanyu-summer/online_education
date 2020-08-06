package com.project.guli.service.trade.controller.api;

import com.github.wxpay.sdk.WXPayUtil;
import com.project.guli.common.base.result.R;
import com.project.guli.common.base.util.StreamUtils;
import com.project.guli.service.trade.entity.Order;
import com.project.guli.service.trade.service.OrderService;
import com.project.guli.service.trade.service.WeiXinPayService;
import com.project.guli.service.trade.util.WeiXinPayProperties;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wan
 * @create 2020-07-30-10:46
 */
@RestController
@RequestMapping("/api/trade/weixin-pay")
@Api(tags = {"网站微信支付"})
//@CrossOrigin
@Slf4j
public class ApiWeixinPayController {
    @Autowired
    private WeiXinPayService weiXinPayService;

    @Autowired
    private WeiXinPayProperties weiXinPayProperties;
    @Autowired
    private OrderService orderService;


    //统一下单接口的调用
    @ApiOperation("根据订单号创建支付")
    @GetMapping("create-native/{orderNo}")
    public R createNative(@PathVariable String orderNo, HttpServletRequest request) {

        String remoteAddr = request.getRemoteAddr();
        Map<String, Object> map = weiXinPayService.createNative(orderNo, remoteAddr);

        return R.ok().data(map);
    }

    //微信发起的回调通知,通知的结果从http请求中拿到，通知返回反馈
    //postmapping中的回调地址一定与请求时url除主机外的地址一致，controller中已有/api/trade/weixin-pay
    @PostMapping("callback/notify")
    public String wxNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("\n callback/notify 被调用");

        //此时通知是以输入流的方式
        ServletInputStream inputStream = request.getInputStream();
        String notifyXml = StreamUtils.inputStream2String(inputStream, "utf-8");
        log.info("\n notifyXml=\n" + notifyXml);

        //验签：验证微信服务器的回调请求中的签名，防止伪造微信服务器
        if (WXPayUtil.isSignatureValid(notifyXml, weiXinPayProperties.getPartnerKey())) {
            //解析返回结果
            Map<String, String> notifyMap = WXPayUtil.xmlToMap(notifyXml);

            //判断支付是否成功
            if ("SUCCESS".equals(notifyMap.get("result_code"))) {
                //金额校验
                String total_fee = notifyMap.get("total_fee");//支付结果返回的订单金额
                //根据订单号查询订单的金额
                String orderNo = notifyMap.get("out_trade_code");
                //防止空指针异常 先拿到order判断是否为空，存在一个逻辑，当订单支付成功后台更新订单状态时将该订单删除了
                Order order = orderService.getOrderByOrderNo(orderNo);
                if (order != null&&order.getTotalFee().intValue()==Integer.parseInt(total_fee)) {

                    //接口调用的幂等性：无论调用接口多少次，影响的结果一致
                    //因此判断订单的状态，为1时是已支付，0是未支付
                    if (order.getStatus() == 0) {//直接响应
                        //更新支付状态
                        //添加更新状态状态的接口
                        orderService.updateOrderStatus(notifyMap);
                    }
                    //支付成功，给微信发送我已接收通知的响应
                    //创建响应对象
                    Map<String, String> resultMap = new HashMap<>();
                    //组装响应参数
                    resultMap.put("result_code", "SUCCESS");
                    resultMap.put("return_msg", "OK");
                    String returnXml = WXPayUtil.mapToXml(resultMap);
                    response.setContentType("text/xml");
                    log.info("支付成功，通知已处理");
                    return returnXml;

                }
            }

        }
        //返回错误信息，微信接收到校验失败的结果后，会反复调用回调函数
        Map<String, String> resultMap = new HashMap<>();
        //组装响应参数
        resultMap.put("result_code", "FAIL");
        resultMap.put("return_msg", "");
        String returnXml = WXPayUtil.mapToXml(resultMap);
        response.setContentType("text/xml");
        log.info("校验失败");
        return returnXml;
    }
}
