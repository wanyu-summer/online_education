package com.project.guli.jwt;

import com.project.guli.jwt.entity.Member;
import com.project.guli.jwt.util.JwtUtils;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;

import javax.security.auth.message.module.ClientAuthModule;

/**
 * @author wan
 * @create 2020-07-29-9:02
 */
public class JwtTest {
    @Test
    public void testGenJwt() {
        Member member = new Member();
        member.setAvatar("wy.jpg");
        member.setId("100");
        member.setNickName("wy");
        System.out.println(JwtUtils.genJwt(member.getId(), member.getNickName(), member.getAvatar()));

    }

    @Test
    public void testParJwt(){
        Claims claims = JwtUtils.checkJwt("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJqdGkiOiIxIiwic3ViIjoiZ3VsaS11c2VyIiwiaWF0IjoxNTk1OTg2NDg2LCJleHAiOjE1OTU5ODgyODYsIm5hbWUiOiIxMDAiLCJuaWNrTmFtZSI6Ind5IiwiYXZhdGFyIjoid3kuanBnIn0.oWG7u0M82-d_mBDcSmp9nSlaXcjMIgRLTcU4__5q3tk");
        String id = (String) claims.get("id");
        String nickName = (String) claims.get("nickName");
        String avatar = (String) claims.get("avatar");
        System.out.println(id);
        System.out.println(nickName);
        System.out.println(avatar);
    }
}
