package com.project.guli.service.trade.service;

import java.util.Map;

/**
 * @author wan
 * @create 2020-07-30-10:02
 */
public interface WeiXinPayService {

    Map<String, Object> createNative(String orderNo, String remoteAddr);

}
