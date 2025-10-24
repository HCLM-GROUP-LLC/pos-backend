package com.hclm.merchant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hclm.merchant.pojo.request.EmployeesAddRequest;
import com.hclm.merchant.pojo.request.EmployeesCopyRequest;
import com.hclm.merchant.pojo.request.EmployeesUpdateRequest;
import com.hclm.merchant.pojo.response.EmployeesCopyResponse;
import com.hclm.merchant.pojo.response.EmployeesResponse;
import com.hclm.mybatis.entity.EmployeeEntity;

import java.util.List;


public interface EmployeesMangerService extends IService<EmployeeEntity> {

    /**
     * 添加员工
     *
     * @param request 请求
     * @return {@link EmployeesResponse }
     */
    EmployeesResponse addEmployees(EmployeesAddRequest request);

    /**
     * 获取员工列表
     *
     * @param storeId 门店id     * @return {@link List }
     */
    List<EmployeesResponse> getEmployeesList(String storeId);

    /**
     * 删除员工
     *
     * @param employeesId 员工id
     */
    void deleteEmployees(String employeesId);

    /**
     * 更新员工
     *
     * @param employeesId 员工id
     * @param request     请求
     * @return {@link EmployeesResponse }
     */
    EmployeesResponse updateEmployees(String employeesId, EmployeesUpdateRequest request);

    /**
     * 获取员工
     *
     * @param employeesId 员工id
     * @return {@link EmployeesResponse }
     */
    EmployeesResponse getEmployees(String employeesId);

    /**
     * 复制员工到其他店铺
     *
     * @param request 请求
     * @return {@link EmployeesCopyResponse }
     */
    EmployeesCopyResponse copyEmployees(EmployeesCopyRequest request);

    /**
     * 复制单个员工到目标店铺
     */
    EmployeesResponse copyEmployeeToStore(String employeeId, EmployeesCopyRequest request);

}
