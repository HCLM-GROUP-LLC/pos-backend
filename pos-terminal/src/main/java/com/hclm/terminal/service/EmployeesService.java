package com.hclm.terminal.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hclm.mybatis.entity.EmployeeEntity;
import com.hclm.terminal.pojo.request.EmployeesLoginRequest;
import com.hclm.terminal.pojo.response.EmployeesLoginResponse;


public interface EmployeesService extends IService<EmployeeEntity> {

    /**
     * 登录
     *
     * @param request 请求
     * @return {@link EmployeesLoginResponse }
     */
    EmployeesLoginResponse login(EmployeesLoginRequest request);

    /**
     * 获取登录信息
     *
     * @return {@link EmployeesLoginResponse }
     */
    EmployeesLoginResponse getLoginInfo();

    void logout();
}
