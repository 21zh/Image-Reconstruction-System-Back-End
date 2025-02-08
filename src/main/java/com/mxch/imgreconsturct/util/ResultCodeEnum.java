package com.mxch.imgreconsturct.util;

import lombok.Getter;

@Getter
public enum ResultCodeEnum {

    SUCCESS(200,"成功"),
    FAIL(201,"失败"),
    TOKEN_EXPIRATE(401,"token过期"),
    NO_POWER(403,"无权访问"),
    NO_ADDRESS(404,"请求地址错误"),
    SERVER_ERROR(500, "服务器出现问题"),
    NO_USER(801,"用户不存在"),
    FAIL_USER(802,"用户名或者密码错误"),
    NO_AVATAR(411,"头像为空"),
    ISUSED_PHONE(412,"手机号已经被使用"),
    ISEXPIRATE_TOKEN(413,"token已经过期，请重新登录"),
    NO_TOKEN(414,"token不存在"),
    LOGOUT_SUCCESS(415,"退出登录")
    ;

    private Integer code;
    private String message;

    private ResultCodeEnum(Integer code, String message){
        this.code = code;
        this.message = message;
    }

}
