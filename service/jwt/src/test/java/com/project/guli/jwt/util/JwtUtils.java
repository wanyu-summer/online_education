package com.project.guli.jwt.util;

import io.jsonwebtoken.*;

import java.util.Date;

/**
 * @author wan
 * @create 2020-07-29-0:14
 */
public class JwtUtils {
    //过期时间
    public static final long EXPIRE = 1000 * 60 * 30;

    //APP secret
    public static final String APP_SECRET = "123456";

    //生成token
    public static String genJwt(String id, String nickName, String avatar) {
        //创建builder对象
        JwtBuilder builder = Jwts.builder();

        //第一部分，jwt 头，header
        builder.setHeaderParam("alg", "HS256");//签名算法
        builder.setHeaderParam("typ", "JWT");//令牌类型

        //有效载荷 Playload
        //默认字段
        builder.setId("1");//jwt唯一身份标识，主要用来作为一次性token，从而避免重放攻击
        builder.setSubject("guli-user");//令牌主题
        builder.setIssuedAt(new Date());//签发时间
        builder.setExpiration(new Date(System.currentTimeMillis() + EXPIRE));//过期时间，当生成的token 到达过期时间 token不可用

        //私有字段
        builder.claim("id", id);
        builder.claim("nickName", nickName);
        builder.claim("avatar", avatar);

        //签名哈希
        builder.signWith(SignatureAlgorithm.HS256, APP_SECRET);

        //将三部分连接起来
        String token = builder.compact();
        return token;
    }

    //解析
    public static Claims checkJwt(String jwtToken) {
        JwtParser parser = Jwts.parser();
        Jws<Claims> claimsJws = parser.setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
        JwsHeader header = claimsJws.getHeader();
        Claims body = claimsJws.getBody();
        String signature = claimsJws.getSignature();
        return body;
    }
}
