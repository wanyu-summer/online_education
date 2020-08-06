package com.project.guli.service.ucenter.controller.api;


import com.project.guli.common.base.result.R;
import com.project.guli.common.base.result.ResultCodeEnum;
import com.project.guli.common.base.util.JwtInfo;
import com.project.guli.common.base.util.JwtUtils;
import com.project.guli.service.base.dto.MemberDto;
import com.project.guli.service.base.exception.GuliException;
import com.project.guli.service.ucenter.entity.vo.LoginVo;
import com.project.guli.service.ucenter.entity.vo.RegisterVo;
import com.project.guli.service.ucenter.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author wwwy
 * @since 2020-07-28
 */
@Api(tags = {"会员管理"})
@RestController
//@CrossOrigin
@RequestMapping("/api/ucenter/member")
@Slf4j
public class ApiMemberController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private RegisterVo registerVo;

    @ApiOperation("会员注册")
    @PostMapping("register")
    public R register(
            @ApiParam(value = "用户信息", required = true)
            @RequestBody RegisterVo registerVo) {
        memberService.register(registerVo);
        return R.ok().message("注册成功");
    }

    @ApiOperation("会员登录")
    @PostMapping("login")
    public R login(
            @RequestBody LoginVo loginVo) {

        String token = memberService.login(loginVo);

        return R.ok().data("token", token).message("登录成功");
    }

    @ApiOperation("根据token获取登录信息")
    @GetMapping("get-login-info")
    public R getLoginInfo(HttpServletRequest request) {
        try {
            JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);
            return R.ok().data("userInfo", jwtInfo);
        } catch (Exception e) {
            log.error("解析用户信息失败，" + e.getMessage());
            throw new GuliException(ResultCodeEnum.FETCH_USERINFO_ERROR);
        }

    }

    @ApiOperation("根据会员id查询会员信息")
    @GetMapping("inner/get-member-dto/{memberId}")
    public MemberDto getMemberDtoById(
            @ApiParam(value = "会员id",required = true)
            @PathVariable String memberId){
        MemberDto memberDto = memberService.getMemberDtoById(memberId);
        return memberDto;
    }


}

