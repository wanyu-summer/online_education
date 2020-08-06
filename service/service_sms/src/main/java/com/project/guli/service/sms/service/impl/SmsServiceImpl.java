package com.project.guli.service.sms.service.impl;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.google.gson.Gson;
import com.project.guli.common.base.result.ResultCodeEnum;
import com.project.guli.service.base.exception.GuliException;
import com.project.guli.service.sms.service.SmsService;
import com.project.guli.service.sms.util.SmsProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.rmi.runtime.Log;

import java.rmi.ServerException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wan
 * @create 2020-07-28-16:51
 */
@Service
@Slf4j
public class SmsServiceImpl implements SmsService {

    @Autowired
    private SmsProperties smsProperties;

    @Override
    public void send(String mobile, String checkCode) throws ClientException {
        //从阿里云短信服务复制代码
        //创建配置对象
        DefaultProfile profile = DefaultProfile.getProfile(
                smsProperties.getRegionId(),
                smsProperties.getKeyId(),
                smsProperties.getKeySecret());

        //创建client对象
        IAcsClient client = new DefaultAcsClient(profile);

        //创建参数对象
        CommonRequest request = new CommonRequest();

        //组装参数对象
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", smsProperties.getRegionId());
        request.putQueryParameter("PhoneNumbers",mobile);
        request.putQueryParameter("SignName",smsProperties.getSignName());
        request.putQueryParameter("TemplateCode",smsProperties.getTemplateCode());
//        request.putQueryParameter("TemplateParam", "{\"code\":\"198345058\"}");
        Map<String, String> param = new HashMap<>();
        param.put("code", checkCode);
        Gson gson = new Gson();
        String json = gson.toJson(param);

        request.putQueryParameter("TemplateParam",json);

        //发送短信
        CommonResponse response = client.getCommonResponse(request);
        String data = response.getData();
        //解析data中的message或code，判断短信息是否发送成功
        HashMap<String,String> map = gson.fromJson(data, HashMap.class);
        System.out.println(map);//{Message=OK, RequestId=E478E67B-CB5C-4C4F-890C-E5C27C2FB887, BizId=971318996158483924^0, Code=OK}

        //注意是大小写,否则取不出数据
        String code = map.get("Code");
        String message = map.get("Message");

        if ("isv.BUSINESS_LIMIT_CONTROL".equals(code)) {
            log.error("短信发送过于频繁"+"code-"+code+"message:"+message);
            throw new GuliException(ResultCodeEnum.SMS_SEND_ERROR_BUSINESS_LIMIT_CONTROL);
        }
        if (!"OK".equals(code)) {
            log.error("短信发送失败"+"code-"+code+"message:"+message);
            throw new GuliException(ResultCodeEnum.SMS_SEND_ERROR);
        }

    }
}
