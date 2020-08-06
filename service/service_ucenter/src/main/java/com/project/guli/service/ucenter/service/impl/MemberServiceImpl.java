package com.project.guli.service.ucenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.BeanProperty;
import com.project.guli.common.base.result.ResultCodeEnum;
import com.project.guli.common.base.util.FormUtils;
import com.project.guli.common.base.util.JwtInfo;
import com.project.guli.common.base.util.JwtUtils;
import com.project.guli.common.base.util.MD5;
import com.project.guli.service.base.dto.MemberDto;
import com.project.guli.service.base.exception.GuliException;
import com.project.guli.service.ucenter.entity.Member;
import com.project.guli.service.ucenter.entity.vo.LoginVo;
import com.project.guli.service.ucenter.entity.vo.RegisterVo;
import com.project.guli.service.ucenter.mapper.MemberMapper;
import com.project.guli.service.ucenter.service.MemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author wwwy
 * @since 2020-07-28
 */
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override

    public void register(RegisterVo registerVo) {

        //首先校验注册的参数
        String code = registerVo.getCode();
        String nickname = registerVo.getNickname();//昵称
        String mobile = registerVo.getMobile();
        String password = registerVo.getPassword();

        //校验电话
        if (StringUtils.isEmpty(mobile) || !FormUtils.isMobile(mobile) || StringUtils.isEmpty(nickname) || StringUtils.isEmpty(password) || StringUtils.isEmpty(code)) {
            throw new GuliException(ResultCodeEnum.LOGIN_MOBILE_ERROR);
        }

        //进行验证码的校验,需获取redis数据
        String checkCode = (String) redisTemplate.opsForValue().get(mobile);
        //code放在前面，而且已经做过非空校验，防止失效checkcode为空，造成空指针异常
        if (!code.equals(checkCode)) {
            throw new GuliException(ResultCodeEnum.CODE_ERROR);
        }

        //校验用户是否注册，从mysql中取值，当从redis中取值错误就不会访问数据库，减少数据库压力
        QueryWrapper<Member> memberQueryWrapper = new QueryWrapper<>();
        memberQueryWrapper.eq("mobile", mobile);

        Integer count = baseMapper.selectCount(memberQueryWrapper);//注意 selectOne的效率比selectCount效率高，且返回值为null或integer类型数据1
        if (count > 0) {
            throw new GuliException(ResultCodeEnum.REGISTER_MOBLE_ERROR);//已被注册
        }

        //执行注册
        Member member = new Member();
//        BeanUtils.copyProperties(registerVo, member);
        member.setNickname(nickname);
        member.setMobile(mobile);
        member.setPassword(MD5.encrypt(password));
        member.setAvatar("https://guli-file-wy.oss-cn-beijing.aliyuncs.com/avatar/2020/07/24/0786197d-08ee-459d-940b-e632c3ff1786.jpg");
        member.setDisabled(false);
        baseMapper.insert(member);
    }

    /**
     * 登录返回一个token
     *
     * @param loginVo
     * @return
     */
    @Override
    public String login(LoginVo loginVo) {
        //校验：参数是否合法
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password) || !FormUtils.isMobile(mobile)) {
            throw new GuliException(ResultCodeEnum.LOGIN_MOBILE_ERROR);
        }
        //校验手机号是否存在
        QueryWrapper<Member> memberQueryWrapper = new QueryWrapper<>();
        memberQueryWrapper.eq("mobile", mobile);
        Member member = baseMapper.selectOne(memberQueryWrapper);
        if (member == null) {
            throw new GuliException(ResultCodeEnum.LOGIN_MOBILE_ERROR);
        }
        //校验密码是否正确
        if (!MD5.encrypt(password).equals(member.getPassword())) {
            throw new GuliException(ResultCodeEnum.LOGIN_PASSWORD_ERROR);
        }
        //检查用户是否被禁用
        if (member.getDisabled()) {
            throw new GuliException(ResultCodeEnum.LOGIN_DISABLED_ERROR);
        }
        //登录：生成token
        JwtInfo info = new JwtInfo();
        info.setId(member.getId());
        info.setNickname(member.getNickname());
        info.setAvatar(member.getAvatar());

        String jwtToken = JwtUtils.getJwtToken(info, 1800);
        return jwtToken;
    }

    @Override
    public MemberDto getMemberDtoById(String memberId) {

        Member member = baseMapper.selectById(memberId);
        MemberDto memberDto = new MemberDto();
        BeanUtils.copyProperties(member, memberDto);
        return memberDto;
    }

    @Override
    public Integer countRegisterNum(String day) {
        //根据注册的时间统计当天注册的会员数，在xml文件中编写了select语句

        return baseMapper.selectRegisterMemberCount(day);
    }
}
