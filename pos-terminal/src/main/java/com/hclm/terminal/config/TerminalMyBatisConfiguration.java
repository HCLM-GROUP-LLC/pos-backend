package com.hclm.terminal.config;

import com.baomidou.mybatisplus.extension.plugins.inner.DataPermissionInterceptor;
import com.hclm.terminal.handler.StoreDataPermHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class TerminalMyBatisConfiguration {
    @Bean
    public DataPermissionInterceptor dataPermissionInterceptor() {
        log.info("数据权限处理程序开启");
        return new DataPermissionInterceptor(new StoreDataPermHandler());
    }
}
