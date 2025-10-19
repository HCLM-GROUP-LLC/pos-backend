package com.hclm.terminal.config;

import com.baomidou.mybatisplus.extension.plugins.inner.DataPermissionInterceptor;
import com.hclm.terminal.handler.TerminalPermissionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TerminalMyBatisConfiguration {
    @Bean
    public DataPermissionInterceptor dataPermissionInterceptor(TerminalPermissionHandler terminalPermissionHandler) {
        return new DataPermissionInterceptor(terminalPermissionHandler);
    }
}
