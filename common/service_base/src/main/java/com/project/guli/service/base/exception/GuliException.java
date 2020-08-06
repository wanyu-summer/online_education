package com.project.guli.service.base.exception;

import com.project.guli.common.base.result.ResultCodeEnum;
import lombok.Data;

/**
 * @author wan
 * @create 2020-07-13-19:54
 */
@Data
public class GuliException extends RuntimeException{
    private Integer code;

    public GuliException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    public GuliException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
    }

    @Override
    public String toString() {
        return "GuliException{" +
                "code=" + code +
                ",message="+this.getMessage()+
                '}';
    }
}
