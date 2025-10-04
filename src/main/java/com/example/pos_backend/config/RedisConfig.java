package com.example.pos_backend.config;

import com.example.pos_backend.common.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class RedisConfig {
    /**
     * redis工具
     *
     * @return {@link RedisUtil }
     */
    @Bean
    public RedisUtil redisUtil(RedissonClient redissonClient) {
        log.info("初始化redis工具类");
        return new RedisUtil(redissonClient);
    }
}
