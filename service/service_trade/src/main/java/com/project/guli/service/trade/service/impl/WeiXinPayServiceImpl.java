package com.project.guli.service.trade.service.impl;

import com.github.wxpay.sdk.WXPayUtil;
import com.project.guli.common.base.result.ResultCodeEnum;
import com.project.guli.common.base.util.ExceptionUtils;
import com.project.guli.service.base.exception.GuliException;
import com.project.guli.service.trade.entity.Order;
import com.project.guli.service.trade.service.OrderService;
import com.project.guli.service.trade.service.WeiXinPayService;
import com.project.guli.service.trade.util.WeiXinPayProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.project.guli.common.base.util.HttpClientUtils;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wan
 * @create 2020-07-30-10:04
 */
@Service
@Slf4j
public class WeiXinPayServiceImpl implements WeiXinPayService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private WeiXinPayProperties weiXinPayProperties;

    @Override
    public Map<String, Object> createNative(String orderNo, String remoteAddr) {

        //根据订单号获取订单
        Order order = orderService.getOrderByOrderNo(orderNo);

        //调用微信的统一下单API
//        HttpClientUtils client = ("https://api.mch.weixin.qq.com/pay/unifiedorder");
        HttpClientUtils client = new HttpClientUtils("https://api.mch.weixin.qq.com/pay/unifiedorder");

        //组装参数
        try {
            Map<String, String> params = new HashMap<>();
            params.put("appid", weiXinPayProperties.getAppId());//公账号id
            params.put("mch_id", weiXinPayProperties.getPartner());//商户号
            params.put("nonce_str", WXPayUtil.generateNonceStr());//随机数
            params.put("body", order.getCourseTitle());//商品描述，课程标题
            params.put("out_trade_no", orderNo);//商品订单号
            params.put("total_fee", order.getTotalFee().intValue()+"");//商品价格,注意要求int，这里将int再转化为字符串，加"",只有包装类才能toString
            params.put("spbill_create_ip", remoteAddr);//终端IP
            params.put("notify_url", weiXinPayProperties.getNotifyUrl());//通知地址（回调地址）
            params.put("trade_type", "NATIVE");//交易类型

            //将参数转换成xml字符串，并且在字符串的最后追加计算的签名
            String xmlParams = WXPayUtil.generateSignedXml(params,weiXinPayProperties.getPartnerKey());
            log.info(xmlParams);

            //将参数放入请求对象的方法体
            client.setXmlParam(xmlParams);
            //使用HTTPS协议传输
            client.setHttps(true);
            //使用post方法发送请求，请求的url地址即微信支付服务器地址,先通过url生成client 再设置xmlParams,然后post
            client.post();
            //得到响应
            String resultXml = client.getContent();

            //将响应结果转成map对象
            Map<String, String> resultMap = WXPayUtil.xmlToMap(resultXml);

            //错误处理
            //return_code 通信标识，业务结果，需同时为true，才能代表下单支付成功
            if("FAIL".equals(resultMap.get("return_code")) || "FAIL".equals(resultMap.get("result_code"))){
                log.error("微信支付统一下单错误 - "
                        + "return_code: " + resultMap.get("return_code")
                        + "return_msg: " + resultMap.get("return_msg")
                        + "result_code: " + resultMap.get("result_code")
                        + "err_code: " + resultMap.get("err_code")
                        + "err_code_des: " + resultMap.get("err_code_des"));

                throw new GuliException(ResultCodeEnum.PAY_UNIFIEDORDER_ERROR);
            }

            //当return_code和result_code都为true时，会返回交易类型，预支付交易会话标识，二维码链接
            //要组装的结果对象
            Map<String, Object> map = new HashMap<>();
            map.put("result_code", resultMap.get("result_code"));//交易结果标识
            map.put("code_url", resultMap.get("code_url"));//二维码链接
            map.put("course_id", order.getId());//课程id
            map.put("total_fee", order.getTotalFee());//课程价格
            map.put("out_trade_no", order.getOrderNo());//订单号

            return map;
        } catch
        (Exception e) {
            log.error(ExceptionUtils.getMessage(e));
            throw new GuliException(ResultCodeEnum.PAY_UNIFIEDORDER_ERROR);
        }

    }
}
