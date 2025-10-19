package com.hclm.web;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.hclm.web.utils.MessageUtil;
import com.hclm.web.utils.PwdUtil;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

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

    /**
     * mybatis元对象处理程序 自动注入 创建时间 修改时间
     *
     * @return {@link MybatisMetaObjectHandler }
     */
    @ConditionalOnMissingBean(MetaObjectHandler.class)
    @Bean
    public MybatisMetaObjectHandler mybatisMetaObjectHandler() {
        log.info("mybatis元对象处理程序开启");
        return new MybatisMetaObjectHandler();
    }

    /**
     * 分页内部拦截器
     *
     * @return {@link PaginationInnerInterceptor }
     */
    @Bean
    public PaginationInnerInterceptor paginationInnerInterceptor() {
        log.info("分页内部拦截器开启");
        return new PaginationInnerInterceptor();
    }

    /**
     * mybatis plus拦截器
     *
     * @param innerInterceptors 内部拦截器
     * @return {@link MybatisPlusInterceptor }
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(List<InnerInterceptor> innerInterceptors) {
        List<String> interceptorNames = innerInterceptors.stream().map(InnerInterceptor::getClass).map(Class::getSimpleName).toList();
        log.info("mybatis plus拦截器开启 {}", interceptorNames);
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.setInterceptors(innerInterceptors);
        return interceptor;
    }
}
