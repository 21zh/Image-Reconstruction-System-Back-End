package com.mxch.imgreconsturct;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RedisTest {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void testString() {
        // 插入数据
        stringRedisTemplate.opsForValue().set("name", "李四");

        // 读取数据
        String name = stringRedisTemplate.opsForValue().get("name");
        System.out.println("name = " + name);
    }
}

