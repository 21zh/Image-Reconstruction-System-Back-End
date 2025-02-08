package com.mxch.imgreconsturct.controller;

import com.mxch.imgreconsturct.pojo.dto.LoginForm;
import com.mxch.imgreconsturct.pojo.dto.RegisterForm;
import com.mxch.imgreconsturct.service.UserService;
import com.mxch.imgreconsturct.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 已有账户登录
     * @param loginForm
     * @return
     */
    @PostMapping("/login")
    public Result login(@RequestBody LoginForm loginForm){
        return userService.login(loginForm);
    }

    /**
     * 发送验证码
     * @param phone
     */
    @PostMapping("/sendCode")
    public Result sendCode(@RequestParam("phone") String phone){
        return userService.sendCode(phone);
    }

    /**
     * 注册账户登录
     * @param registerForm
     * @return
     */
    @PostMapping("/register")
    public Result register(@RequestBody RegisterForm registerForm){
        return userService.register(registerForm);
    }

    /**
     * 获取部分用户信息
     * @param request
     * @return
     */
    @GetMapping("/getUserPart")
    public Result userPart(HttpServletRequest request){
        return userService.userPart(request);
    }

    @PostMapping("/logout")
    public Result logout(HttpServletRequest request) {
        return userService.logout(request);
    }

}
