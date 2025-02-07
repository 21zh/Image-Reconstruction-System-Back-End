package com.mxch.imgreconsturct.util;

import lombok.Getter;

@Getter
public enum ResultCodeEnum {

    SUCCESS(200,"成功"),
    FAIL(201,"失败"),
    TOKEN_EXPIRATE(401,"token过期"),
    NO_POWER(403,"无权访问"),
    NO_ADDRESS(404,"请求地址错误"),
    SERVER_ERROR(500, "服务器出现问题");

    private Integer code;
    private String message;

    private ResultCodeEnum(Integer code, String message){
        this.code = code;
        this.message = message;
    }

}
