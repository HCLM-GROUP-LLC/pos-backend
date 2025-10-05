package com.example.pos_backend.config;

import com.example.pos_backend.handler.DeviceOnlineHandler;
import com.example.pos_backend.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@RequiredArgsConstructor
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final DeviceService deviceService;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 注册 DeviceOnlineHandler，映射到特定的 WebSocket 路径
        registry.addHandler(new DeviceOnlineHandler(deviceService), "/api/devices/online/*")
                .setAllowedOrigins("*"); // 根据需要设置允许的来源
    }
}
