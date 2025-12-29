package com.mxch.imgreconsturct.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mxch.imgreconsturct.mapper.UserMapper;
import com.mxch.imgreconsturct.pojo.User;
import com.mxch.imgreconsturct.pojo.dto.LoginForm;
import com.mxch.imgreconsturct.pojo.dto.RegisterForm;
import com.mxch.imgreconsturct.pojo.dto.UserPart;
import com.mxch.imgreconsturct.service.UserService;
import com.mxch.imgreconsturct.util.MD5;
import com.mxch.imgreconsturct.util.Result;
import com.mxch.imgreconsturct.util.ResultCodeEnum;
import com.mxch.imgreconsturct.util.SendCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.mxch.imgreconsturct.util.RedisConstants.*;
import static com.mxch.imgreconsturct.util.ResultCodeEnum.*;

@Slf4j
@Service
@Transactional
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    /**
     * 登录逻辑处理
     * @param loginForm
     * @return
     */
    @Override
    public Result login(LoginForm loginForm) {
        log.info("登录功能");
        // 用户名和密码
        String userName = loginForm.getUserName();
        String password = loginForm.getPassword();

        // 根据用户名查询用户是否存在
        User user = query().eq("user_name", userName).one();

        // 如果用户为空，即为注册登录
        if (user == null){
            return Result.build(null, ResultCodeEnum.NO_USER);
        }

        // 判断密码是否正确
        String encryptPassword = MD5.encrypt(password);
        // 密码相同
        if(encryptPassword.equals(user.getPassword())){
            // 生成token
            String token = UUID.randomUUID().toString(true);
            // 将用户信息和token存入到redis中
            UserPart uerPart = BeanUtil.copyProperties(user,UserPart.class);
            // 将对象转成hashMap存储
            Map<String, Object> userPartMap = BeanUtil.beanToMap(uerPart, new HashMap<>(),
                    CopyOptions.create()
                            .setIgnoreNullValue(true)
                            .setFieldValueEditor((fieldName, fieldValue) -> fieldValue.toString()));
            String tokenKey = TOKEN_USER_KEY + token;
            stringRedisTemplate.opsForHash().putAll(tokenKey,userPartMap);
            // 设置过期时间
            stringRedisTemplate.expire(tokenKey, TOKEN_USER_TTL, TimeUnit.HOURS);
            return Result.ok(token);
        }

        return Result.build(null,ResultCodeEnum.FAIL_USER);
    }

    /**
     * 注册逻辑处理
     * @param registerForm
     * @return
     */
    @Override
    public Result register(RegisterForm registerForm) {
        // 校验手机号和验证码是否正确
        String phone = registerForm.getPhone();
        String code = registerForm.getCode();

        // 从redis中取出验证码
        String rightCode = stringRedisTemplate.opsForValue().get(LOGIN_CODE_KEY + phone);
        // 校验验证码是否正确
        if (!Objects.equals(code, rightCode)){
            return Result.fail();
        }
        // 校验成功
        // 新建用户对象，将registerForm的属性赋值给用户
        User user = new User();
        BeanUtils.copyProperties(registerForm,user,"code","password");
        user.setPassword(MD5.encrypt(registerForm.getPassword()));
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());

        // 将用户信息添加到数据库中
        save(user);

        // 生成token
        String token = UUID.randomUUID().toString(true);
        // 将用户信息和token存入到redis中
        UserPart userPart = BeanUtil.copyProperties(user,UserPart.class);
        userPart.setRoleId(1);
        // 将对象转成hashMap存储
        Map<String, Object> userPartMap = BeanUtil.beanToMap(userPart, new HashMap<>(),
                CopyOptions.create()
                        .setIgnoreNullValue(true)
                        .setFieldValueEditor((fieldName, fieldValue) -> fieldValue.toString()));
        String tokenKey = TOKEN_USER_KEY + token;
        stringRedisTemplate.opsForHash().putAll(tokenKey,userPartMap);
        // 设置过期时间
        stringRedisTemplate.expire(tokenKey, TOKEN_USER_TTL, TimeUnit.HOURS);
        return Result.ok(token);
    }

    /**
     * 生成验证码
     * @param phone
     * @return
     */
    @Override
    public Result sendCode(String phone) {
        // 查看手机号是否已经注册
        User user = query().eq("phone", phone).one();
        // 如果用户存在
        if(user != null){
            return Result.build(null,ISUSED_PHONE);
        }
        // 生成验证码
        String code = RandomUtil.randomNumbers(6);
        // 发送验证码
        try {
            SendCode.sendCodeMessage(phone, code);
            // 将手机号和验证码保存到redis中
            stringRedisTemplate.opsForValue().set(LOGIN_CODE_KEY + phone,code,LOGIN_CODE_TTL, TimeUnit.MINUTES);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 短信发送验证码
        log.info("验证码为" + code);
        return Result.ok();
    }

    /**
     * 获取部分用户对象
     * @param request
     * @return
     */
    @Override
    public Result userPart(HttpServletRequest request) {
        // 从请求头获取token
        String token = request.getHeader("token");
        // token为空
        if (token == null){
            return Result.build(null,NO_TOKEN);
        }

        // redis存储token的key
        String tokenKey = TOKEN_USER_KEY + token;
        // 在redis中查询用户
        Map<Object, Object> userMap = stringRedisTemplate.opsForHash().entries(tokenKey);

        // token过期
        if(userMap.isEmpty()){
            return Result.build(null,ISEXPIRATE_TOKEN);
        }

        // 将map转化为userPart
        ObjectMapper objectMapper = new ObjectMapper();

        // 返回用户信息
        return Result.ok(objectMapper.convertValue(userMap,UserPart.class));
    }

    /**
     * 退出登录逻辑处理
     * @return
     */
    @Override
    public Result logout(HttpServletRequest request) {
        // 清除redis中的token
        // 获取token
        String token = request.getHeader("token");
        // 构建token关键字
        String tokenKey = TOKEN_USER_KEY + token;
        log.info(tokenKey);
        // 判断tokenkey是否存在
        Boolean isToken = stringRedisTemplate.hasKey(tokenKey);
        // 存在则删除
        if (Boolean.TRUE.equals(isToken))   {
            stringRedisTemplate.delete(tokenKey);
            return Result.build(null,LOGOUT_SUCCESS);
        }
        return Result.ok();
    }
}
