package com.hclm.terminal.utils;

import cn.dev33.satoken.stp.StpUtil;
import com.hclm.mybatis.entity.EmployeeEntity;
import com.hclm.terminal.converter.EmployeesConverter;
import com.hclm.terminal.pojo.cache.EmployeesLoginCache;
import com.hclm.terminal.pojo.response.EmployeesLoginResponse;
import com.hclm.web.utils.JsonUtil;

public class EmployeesLoginUtil {
    private static final String DATA_KEY = "data";

    /**
     * 检查登录
     *
     */
    public static void checkLogin() {
        StpUtil.checkLogin();
    }

    /**
     * 获取登录id
     *
     * @return {@link String }
     */
    public static String getLoginId() {
        return StpUtil.getLoginId().toString();
    }

    /**
     * 是否已经登录
     */
    public static boolean isLogin(String id) {
        return StpUtil.isLogin(id);//判断是否已经登录
    }

    /**
     * 登录
     *
     * @param id   id
     * @param data 数据
     */
    public static void login(String id, EmployeesLoginCache data) {
        StpUtil.login(id);
        StpUtil.getSession().set(DATA_KEY, data);
    }

    /**
     * 注销当前登录
     *
     */
    public static void logout() {
        StpUtil.kickout(StpUtil.getLoginId());//踢出其他登录，彻底清理账号session
        StpUtil.logout();//退出当前登录,清理token session
    }

    /**
     * 获取登录信息缓存
     *
     * @return {@link EmployeesLoginCache }
     */
    public static EmployeesLoginCache loginCache() {
        if (!StpUtil.isLogin()) {
            return null;
        }
        Object data = StpUtil.getSession().get(DATA_KEY);
        return JsonUtil.convertObject(data, EmployeesLoginCache.class);
    }

    /**
     * 登录响应
     *
     * @param employeeEntity 员工
     * @return {@link EmployeesLoginResponse }
     */
    public static EmployeesLoginResponse toLoginResponse(EmployeeEntity employeeEntity) {
        //转换为登录响应
        EmployeesLoginResponse response = EmployeesConverter.INSTANCE.toLoginResponse(employeeEntity);
        response.setTokenName(StpUtil.getTokenName());
        response.setTokenValue(StpUtil.getTokenValue());
        return response;
    }
}
