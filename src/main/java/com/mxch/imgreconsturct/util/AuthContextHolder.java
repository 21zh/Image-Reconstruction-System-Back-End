package com.mxch.imgreconsturct.util;

import com.mxch.imgreconsturct.pojo.User;

import javax.servlet.http.HttpServletRequest;

public class AuthContextHolder {
    // 从请求头获取token，解析出用户对象
    public static User getUserToken(HttpServletRequest request){
        // 获取token
        String token = request.getHeader("token");
        // 调用工具类
        User user = JwtHelper.getUser(token);
        return user;
    }
}
