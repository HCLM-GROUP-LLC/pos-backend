package com.hclm.terminal.config;

import com.hclm.terminal.handler.DeviceOnlineHandler;
import com.hclm.terminal.handler.StoreDataPermInterceptor;
import com.hclm.terminal.handler.StoreDeviceInterceptor;
import com.hclm.terminal.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableWebSocket
public class TerminalConfiguration implements WebSocketConfigurer, WebMvcConfigurer {
    private final DeviceService deviceService;
    private final StoreDeviceInterceptor storeDeviceInterceptor;
    private final StoreDataPermInterceptor storeDataPermInterceptor;

    /**
     * 注册web套接字处理程序
     *
     * @param registry 登记处
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 注册 DeviceOnlineHandler，映射到特定的 WebSocket 路径
        registry.addHandler(new DeviceOnlineHandler(deviceService), "/devices/online/*")
                .setAllowedOrigins("*"); // 根据需要设置允许的来源
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 基本上本模块的所有请求都来自门店的终端，所以添加门店数据权限拦截器
        List<String> excludePathPatterns = List.of(
                "/devices/**",// 排除设备登录请求
                "/**/*.html",       // 排除所有HTML结尾的请求
                "/**/*.css",//样式文件
                "/**/*.js",//脚本文件
                "/**/*.png",//图片文件
                "/**/*.jpg",//图片文件
                "/**/*.jpeg",//图片文件
                "/v3/api-docs/**" // swagger
        );
        // 添加门店设备请求拦截器
        registry.addInterceptor(storeDeviceInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(excludePathPatterns);
        // 添加门店数据权限拦截器
        registry.addInterceptor(storeDataPermInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(excludePathPatterns)
                .excludePathPatterns("/employees-auth/**"); // 排除员工登录请求
    }

}
