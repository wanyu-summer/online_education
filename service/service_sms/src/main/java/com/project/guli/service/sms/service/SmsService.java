package com.project.guli.service.sms.service;

import com.aliyuncs.exceptions.ClientException;

/**
 * @author wan
 * @create 2020-07-28-16:50
 */
public interface SmsService {

    void send(String mobile, String checkCode) throws ClientException;
}
