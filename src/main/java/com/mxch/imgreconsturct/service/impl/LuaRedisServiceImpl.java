package com.mxch.imgreconsturct.service.impl;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.Arrays;

import static com.mxch.imgreconsturct.util.RedisConstants.ROOM_COUNT_KEY;
import static com.mxch.imgreconsturct.util.RedisConstants.USER_LOCK_KEY;

@Service
public class LuaRedisServiceImpl {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private DefaultRedisScript<Long> script;

    public Long execute(String userId, String roomId) {
        String roomKey = ROOM_COUNT_KEY + roomId;
        String lockKey = USER_LOCK_KEY + userId + ":" + roomId + ":";

        return stringRedisTemplate.execute(
                script,
                Arrays.asList(roomKey, lockKey),
                "1",
                "60"
        );
    }

}
