package com.project.guli.service.base.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wan
 * @create 2020-07-29-17:40
 */
@Data
public class MemberDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;//会员id
    private String nickname;//昵称
    private String mobile;//手机号

}
