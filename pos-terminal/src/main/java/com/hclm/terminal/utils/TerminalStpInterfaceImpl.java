package com.hclm.terminal.utils;

import cn.dev33.satoken.stp.StpInterface;
import com.hclm.terminal.pojo.cache.EmployeesLoginCache;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 终端stp接口实现
 *
 * @author hanhua
 * @since 2025/10/10
 */
@Component
public class TerminalStpInterfaceImpl implements StpInterface {
    /**
     * 获取权限列表 暂时不需要 因为角色权限是固定的
     *
     * @param loginId   登录id
     * @param loginType 登录类型
     * @return {@link List }<{@link String }>
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return List.of();
    }

    /**
     * 获取角色列表
     *
     * @param loginId   登录id
     * @param loginType 登录类型
     * @return {@link List }
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        EmployeesLoginCache cache = EmployeesLoginUtil.loginCache();
        return List.of(cache.getRoleId());
    }
}
