package com.project.guli.service.ucenter.service;

import com.project.guli.service.base.dto.MemberDto;
import com.project.guli.service.ucenter.entity.Member;
import com.baomidou.mybatisplus.extension.service.IService;
import com.project.guli.service.ucenter.entity.vo.LoginVo;
import com.project.guli.service.ucenter.entity.vo.RegisterVo;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author wwwy
 * @since 2020-07-28
 */
public interface MemberService extends IService<Member> {

    void register(RegisterVo registerVo);

    String login(LoginVo loginVo);

    MemberDto getMemberDtoById(String memberId);

    Integer countRegisterNum(String day);
}
