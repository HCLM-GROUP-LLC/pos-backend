package com.hclm.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Slf4j
@MapperScan("com.hclm.**.mapper")
@Configuration
public class MybatisConfiguration {
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
     * mybatis plus拦截器
     * {@see MybatisPlusInnerInterceptorAutoConfiguration}
     *
     * @param innerInterceptors 内部拦截器
     * @return {@link MybatisPlusInterceptor }
     */
    @ConditionalOnMissingBean(MybatisPlusInterceptor.class) // 如果容器中没有这个bean
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(List<InnerInterceptor> innerInterceptors) {
        List<String> interceptorNames = innerInterceptors.stream().map(InnerInterceptor::getClass).map(Class::getSimpleName).toList();
        log.info("mybatis plus拦截器开启 {}", interceptorNames);
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.setInterceptors(innerInterceptors);
        return interceptor;
    }
}
