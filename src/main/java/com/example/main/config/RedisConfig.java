package com.example.main.config;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfig {
    @Bean
    public StatefulRedisConnection<String, String> connection() {
        var redisClient = RedisClient.create("redis://localhost:6379");
        return redisClient.connect();
    }
}
