package com.project.guli.service.ucenter.mapper;

import com.project.guli.service.ucenter.entity.Member;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 会员表 Mapper 接口
 * </p>
 *
 * @author wwwy
 * @since 2020-07-28
 */
public interface MemberMapper extends BaseMapper<Member> {
    Integer selectRegisterMemberCount(String day);
}
