package com.hclm.web;

import com.hclm.web.utils.MessageUtil;
import com.hclm.web.utils.PwdUtil;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * web配置
 *
 * @author hanhua
 * @since 2025/10/06
 */
@Slf4j
@MapperScan("com.hclm.**.mapper")
@Configuration
public class WebConfiguration {

    /**
     * 全局异常处理程序
     *
     * @return {@link GlobalExceptionHandler }
     */
    @Bean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

    /**
     * 消息工具
     *
     * @param messageSource 消息来源
     * @return {@link MessageUtil }
     */
    @Bean
    public MessageUtil messageUtil(MessageSource messageSource) {
        return new MessageUtil(messageSource);
    }

    /**
     * 密码编码器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PwdUtil pwdUtil(PasswordEncoder passwordEncoder) {
        return new PwdUtil(passwordEncoder);
    }


}
