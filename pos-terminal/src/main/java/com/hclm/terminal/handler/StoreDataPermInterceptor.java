package com.hclm.terminal.handler;

import com.hclm.terminal.utils.EmployeesLoginUtil;
import com.hclm.terminal.utils.StoreContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 门店数据权限拦截器
 *
 * @author hanhua
 * @since 2025/10/27
 */
@Component
@Slf4j
public class StoreDataPermInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        log.info("门店数据权限拦截 {}", request.getRequestURI());
        // 员工登录之后 才需要拦截
        if (EmployeesLoginUtil.isLogin()) {
            var cache = EmployeesLoginUtil.loginCache();
            StoreContext.setStoreId(cache.getStoreId());// 开启门店数据权限
        }
        return true;
    }
}
