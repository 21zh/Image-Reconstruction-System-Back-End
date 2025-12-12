package com.mxch.imgreconsturct.config;

import com.mxch.imgreconsturct.util.UserThreadLocal;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Map;

import static com.mxch.imgreconsturct.util.RedisConstants.TOKEN_USER_KEY;

@Component
public class RequestContextInterceptor implements HandlerInterceptor {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        String tokenKey = TOKEN_USER_KEY + token;
        if (Boolean.FALSE.equals(stringRedisTemplate.hasKey(tokenKey))) {
            return false;
        }

        // 获取user部分信息
        Map<Object, Object> userPart = stringRedisTemplate.opsForHash().entries(tokenKey);
        UserThreadLocal.setUserId((String) userPart.get("id"));

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserThreadLocal.clear();
    }
}
