package com.mxch.imgreconsturct.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {

    @Bean
    public Queue roomQueue() {
        return new Queue("room.queue");
    }

    @Bean
    public Queue aiQueue() {
        return new Queue("ai.queue");
    }

}
