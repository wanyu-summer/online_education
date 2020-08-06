package com.project.guli.service.sms.controller;

import com.aliyuncs.exceptions.ClientException;
import com.project.guli.common.base.result.R;
import com.project.guli.common.base.result.ResultCodeEnum;
import com.project.guli.common.base.util.FormUtils;
import com.project.guli.common.base.util.RandomUtils;
import com.project.guli.service.base.exception.GuliException;
import com.project.guli.service.sms.service.SmsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

/**
 * @author wan
 * @create 2020-07-28-16:49
 */
@RestController
@RequestMapping("/api/sms")
@Api(tags = {"短信管理"})
//@CrossOrigin
@Slf4j
public class ApiSmsController {

    @Autowired
    private SmsService smsService;

    //注入redis，短信验证码进行校验时，需存到redis，才能进行对比
    @Autowired
    private RedisTemplate redisTemplate;

    @ApiOperation("")
    @GetMapping("send/{mobile}")
    public R getCode(@PathVariable String mobile) throws ClientException {

        //校验手机号是否合法
        if (StringUtils.isEmpty(mobile) || !FormUtils.isMobile(mobile)) {
            log.error("手机号不正确");
            throw new GuliException(ResultCodeEnum.LOGIN_MOBILE_ERROR);
        }
        //生成验证码
        String checkCode = RandomUtils.getFourBitRandom();

        //发送验证码
        smsService.send(mobile, checkCode);
        //存储验证码到redis
        redisTemplate.opsForValue().set(mobile, checkCode,30, TimeUnit.DAYS);//只存储5分钟

        return R.ok().message("短信发送成功");
    }
}
