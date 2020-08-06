package com.project.guli.service.ucenter.entity.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 前端网站登录所需的 手机号 密码
 *
 * @author wan
 * @create 2020-07-29-9:52
 */
@Data
public class LoginVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String mobile;
    private String password;
}
