package com.mxch.imgreconsturct.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mxch.imgreconsturct.pojo.User;
import com.mxch.imgreconsturct.pojo.dto.LoginForm;
import com.mxch.imgreconsturct.pojo.dto.RegisterForm;
import com.mxch.imgreconsturct.util.Result;

import javax.servlet.http.HttpServletRequest;

public interface UserService extends IService<User> {
    public Result login(LoginForm loginForm);

    public Result register(RegisterForm registerForm);

    public Result sendCode(String phone);

    public Result userPart(HttpServletRequest request);

    public Result logout(HttpServletRequest request);
}
