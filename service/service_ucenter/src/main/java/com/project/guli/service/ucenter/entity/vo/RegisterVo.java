package com.project.guli.service.ucenter.entity.vo;

import lombok.Data;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * 前端网站注册 所需的昵称 手机号 验证码 密码
 * @author wan
 * @create 2020-07-28-20:02
 */
@Data
@Repository
public class RegisterVo implements Serializable {
    private static final long serialVersionUID=1L;

    private String nickname;
    private String mobile;
    private String password;
    private String code;

}
