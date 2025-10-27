package com.hclm.terminal.handler;

import com.hclm.terminal.service.DeviceService;
import com.hclm.terminal.utils.StoreContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 门店设备请求拦截器
 *
 * @author hanhua
 * @since 2025/10/27
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class StoreDeviceInterceptor implements HandlerInterceptor {
    private final DeviceService deviceService;

    /**
     * 预处理
     *
     * @param request  请求
     * @param response 响应
     * @param handler  处理程序
     * @return boolean
     * @throws Exception 例外
     */
    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler)
            throws Exception {
        log.info("门店请求拦截 {}", request.getRequestURI());
        String deviceId = request.getHeader("Device-Id");
        if (deviceId == null || deviceId.isEmpty()) {
            log.info("设备id为空 {}", request.getRequestURI());
            return false;// 返回false中断执行
        }
        var deviceEntity = deviceService.getById(deviceId);
        if (deviceEntity == null) {
            log.info("设备不存在 {} {}", request.getRequestURI(), deviceId);
            return false;// 返回false中断执行
        }
        StoreContext.setDeviceId(deviceId);
//        StoreContext.enable();// 开启门店数据权限
        return true; // 返回true继续执行，返回false中断执行
    }
}
