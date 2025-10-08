package com.hclm.terminal.handler;

import com.hclm.terminal.service.DeviceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@RequiredArgsConstructor
public class DeviceOnlineHandler extends TextWebSocketHandler {
    private final DeviceService deviceService;

    private String extractDeviceIdFromUri(WebSocketSession session) {
        // 从URI中提取 deviceId 参数
        String uriPath = session.getUri().getPath();
        // 解析路径获取 deviceId 值
        // 例如：/api/devices/online/123 -> deviceId = 123
        String[] parts = uriPath.split("/");
        return parts[parts.length - 1];
    }

    /**
     * 连接建立后
     *
     * @param session 会话
     */
    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) {
        String deviceId = extractDeviceIdFromUri(session);
        log.info("设备上线: {}", deviceId);
        // 连接建立时自动触发在线状态刷新
        deviceService.login(deviceId);
    }

    /**
     * 处理文本消息
     *
     * @param session 会话
     * @param message 信息
     */
    @Override
    public void handleTextMessage(@NonNull WebSocketSession session, @NonNull TextMessage message) {
        String deviceId = extractDeviceIdFromUri(session);
        log.info("设备收到消息: {}", deviceId);
        // 处理在线状态刷新
        deviceService.online(deviceId);
    }

    /**
     * 连接关闭后
     *
     * @param session 会话
     * @param status  状态
     */
    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) {
        String deviceId = extractDeviceIdFromUri(session);
        log.info("设备离线: {}", deviceId);
        deviceService.offline(deviceId);
    }
}
