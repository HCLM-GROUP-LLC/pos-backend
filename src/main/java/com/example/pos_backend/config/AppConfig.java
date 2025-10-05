package com.example.pos_backend.config;

import com.example.pos_backend.common.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j

@Configuration
public class AppConfig {
    @Bean
    public MessageUtil configureMessageUtil(MessageSource messageSource) {
        return new MessageUtil(messageSource);
    }

}
