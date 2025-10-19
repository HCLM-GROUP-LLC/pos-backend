package com.hclm.merchant.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hclm.merchant.converter.EmployeesConverter;
import com.hclm.merchant.pojo.request.EmployeesAddRequest;
import com.hclm.merchant.pojo.request.EmployeesCopyRequest;
import com.hclm.merchant.pojo.request.EmployeesUpdateRequest;
import com.hclm.merchant.pojo.response.EmployeesCopyResponse;
import com.hclm.merchant.pojo.response.EmployeesResponse;
import com.hclm.merchant.service.EmployeesMangerService;
import com.hclm.web.BusinessException;
import com.hclm.web.entity.Employees;
import com.hclm.web.enums.EmployeesRoleEnum;
import com.hclm.web.enums.EmployeesSatusEnum;
import com.hclm.web.enums.ResponseCode;
import com.hclm.web.mapper.EmployeesMapper;
import com.hclm.web.utils.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class EmployeesMangerServiceImpl extends ServiceImpl<EmployeesMapper, Employees> implements EmployeesMangerService {
    /**
     * 添加员工
     *
     * @param request 请求
     * @return {@link EmployeesResponse }
     */
    @Override
    public EmployeesResponse addEmployees(EmployeesAddRequest request) {
        Employees employees = EmployeesConverter.INSTANCE.toEntity(request);
        employees.setEmployeesId(RandomUtil.generateEmployeesId());
        save(employees);//保存员工
        return EmployeesConverter.INSTANCE.toResponse(employees);
    }

    /**
     * 获取员工列表
     *
     * @param storeId 门店id     * @return {@link List }
     */
    public List<EmployeesResponse> getEmployeesList(String storeId) {
        return EmployeesConverter.INSTANCE.toResponseList(lambdaQuery().eq(Employees::getStoreId, storeId).list());
    }

    /**
     * 删除员工
     *
     * @param employeesId 员工id
     */
    public void deleteEmployees(String employeesId) {
        boolean num = removeById(employeesId);
        log.info("删除员工：{}，结果：{}", employeesId, num);
    }

    /**
     * 更新员工
     *
     * @param employeesId 员工id
     * @param request     请求
     * @return {@link EmployeesResponse }
     */
    public EmployeesResponse updateEmployees(String employeesId, EmployeesUpdateRequest request) {
        Employees employees = getOptById(employeesId)
                .orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYEES_NOT_FOUND));
        //转换,赋值,跳过null字段
        Employees newEmployees = EmployeesConverter.INSTANCE.toEntity(request);
        newEmployees.setEmployeesId(employeesId);
        updateById(newEmployees);//更新员工,会自动跳过null字段
        //赋值给老员工,并响应
        EmployeesConverter.INSTANCE.copyEntity(employees, newEmployees);
        return EmployeesConverter.INSTANCE.toResponse(employees);
    }

    /**
     * 获取员工
     *
     * @param employeesId 员工id
     * @return {@link EmployeesResponse }
     */
    public EmployeesResponse getEmployees(String employeesId) {
        Employees employees = getOptById(employeesId)
                .orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYEES_NOT_FOUND));
        return EmployeesConverter.INSTANCE.toResponse(employees);
    }

    /**
     * 复制员工到其他店铺
     *
     * @param request 请求
     * @return {@link EmployeesCopyResponse }
     */
    public EmployeesCopyResponse copyEmployees(EmployeesCopyRequest request) {
        //1.验证原店铺和目标店铺不同并且目标店铺属于同一个商户
        if (request.getSourceStoreId().equals(request.getTargetStoreId())) {
            throw new IllegalArgumentException("sourceStoreId and targetStoreId can not be same");
        }
        //2.批量复制员工
        EmployeesCopyResponse response = new EmployeesCopyResponse();
        for (String employeeId : request.getEmployeeIds()) {
            try {
                EmployeesResponse copiedEmployee = copyEmployeeToStore(employeeId, request);
                response.getSuccessEmployees().add(copiedEmployee);
            } catch (Exception e) {
                EmployeesCopyResponse.FailedEmployee failedEmployee = new EmployeesCopyResponse.FailedEmployee();
                failedEmployee.setEmployeeId(employeeId);
                failedEmployee.setReason(e.getMessage());
                response.getFailedEmployees().add(failedEmployee);
            }
        }

        response.setSuccessCount(response.getSuccessEmployees().size());
        response.setFailedCount(response.getFailedEmployees().size());

        return response;

    }

    /**
     * 复制单个员工到目标店铺
     */
    public EmployeesResponse copyEmployeeToStore(String employeeId, EmployeesCopyRequest request) {
        // 1. 查找源员工
        Employees sourceEmployee = getOptById(employeeId)
                .orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYEES_NOT_FOUND));

        // 2. 验证员工属于源店铺和当前商家
        if (!sourceEmployee.getStoreId().equals(request.getSourceStoreId())) {
            throw new IllegalArgumentException("员工不属于指定的源店铺或商家");
        }

        // 3. 检查目标店铺是否已存在相同邮箱的员工
        if (sourceEmployee.getEmail() != null) {
            boolean emailExists = lambdaQuery().eq(Employees::getEmail, sourceEmployee.getEmail())
                    .eq(Employees::getStoreId, request.getTargetStoreId())
                    .exists();
            if (emailExists) {
                throw new IllegalArgumentException("目标店铺已存在相同邮箱的员工");
            }
        }

        // 4. 创建新员工实体
        Employees newEmployee = new Employees();
        newEmployee.setEmployeesId(RandomUtil.generateEmployeesId());
        newEmployee.setStoreId(request.getTargetStoreId());
        newEmployee.setEmail(sourceEmployee.getEmail());
        newEmployee.setFirstName(sourceEmployee.getFirstName());
        newEmployee.setLastName(sourceEmployee.getLastName());
        newEmployee.setPhoneNumber(sourceEmployee.getPhoneNumber());

        // 5. 设置角色和状态
        if (request.getKeepOriginalRole()) {
            newEmployee.setRoleId(sourceEmployee.getRoleId());
        } else {
            newEmployee.setRoleId(EmployeesRoleEnum.Cashier); // 默认角色
        }

        if (request.getKeepOriginalStatus()) {
            newEmployee.setStatus(sourceEmployee.getStatus());
        } else {
            newEmployee.setStatus(EmployeesSatusEnum.ACTIVE); // 默认状态
        }

        // 6. 保存新员工
        save(newEmployee);

        return EmployeesConverter.INSTANCE.toResponse(newEmployee);
    }
}
