package com.mxch.imgreconsturct.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;

@Configuration
public class RedisLuaConfig {

    @Bean
    public DefaultRedisScript<Long> luaScript() {
        DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        script = new DefaultRedisScript<>();
        script.setLocation(new ClassPathResource("lua/luaUtils.lua"));
        script.setResultType(Long.class);

        return script;
    }
}
