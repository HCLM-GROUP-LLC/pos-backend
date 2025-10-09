package com.hclm.terminal.utils;

import com.hclm.satoken.SaTokenUtil;
import com.hclm.terminal.mapper.EmployeesMapper;
import com.hclm.terminal.pojo.cache.EmployeesLoginCache;
import com.hclm.terminal.pojo.response.EmployeesLoginResponse;
import com.hclm.web.entity.Employees;

public class EmployeesLoginUtil {
    /**
     * 登录
     *
     * @param id   id
     * @param data 数据
     */
    public static void login(String id, EmployeesLoginCache data) {
        SaTokenUtil.login(id, data);
    }

    /**
     * 获取登录信息缓存
     *
     * @return {@link EmployeesLoginCache }
     */
    public static EmployeesLoginCache loginCache() {
        return SaTokenUtil.getData(EmployeesLoginCache.class);
    }

    /**
     * 登录响应
     *
     * @param employees 员工
     * @return {@link EmployeesLoginResponse }
     */
    public static EmployeesLoginResponse toLoginResponse(Employees employees) {
        //转换为登录响应
        EmployeesLoginResponse response = EmployeesMapper.INSTANCE.toLoginResponse(employees);
        response.setTokenName(SaTokenUtil.getTokenName());
        response.setTokenValue(SaTokenUtil.getTokenValue());
        return response;
    }
}
