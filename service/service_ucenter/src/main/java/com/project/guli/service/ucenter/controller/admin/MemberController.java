package com.project.guli.service.ucenter.controller.admin;

import com.project.guli.common.base.result.R;
import com.project.guli.service.ucenter.entity.Member;
import com.project.guli.service.ucenter.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wan
 * @create 2020-08-01-16:51
 */
@Api(tags = {"会员管理"})
@RestController
@RequestMapping("/admin/ucenter/member")
@Slf4j
public class MemberController {
    @Autowired
    private MemberService memberService;

    @ApiOperation("根据日期统计用户注册数")
    @GetMapping("/count-register-num/{day}")
    public R countRegisterNum(
            @ApiParam(value = "统计日期", required = true)
            @PathVariable String day) {

        Integer integer = memberService.countRegisterNum(day);
        return R.ok().data("registerNum", integer);
    }
}
