package com.mxch.imgreconsturct.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {

    @Bean
    public Queue simpleQueue() {
        return new Queue("room.queue");
    }
}
