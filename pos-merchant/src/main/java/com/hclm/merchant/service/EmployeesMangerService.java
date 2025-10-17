package com.hclm.merchant.service;

import com.hclm.merchant.mapper.EmployeesManagerMapper;
import com.hclm.merchant.pojo.request.EmployeesAddRequest;
import com.hclm.merchant.pojo.request.EmployeesCopyRequest;
import com.hclm.merchant.pojo.request.EmployeesUpdateRequest;
import com.hclm.merchant.pojo.response.EmployeesCopyResponse;
import com.hclm.merchant.pojo.response.EmployeesResponse;
import com.hclm.merchant.utils.MerchantLoginUtil;
import com.hclm.web.BusinessException;
import com.hclm.web.entity.Employees;
import com.hclm.web.enums.EmployeesRoleEnum;
import com.hclm.web.enums.EmployeesSatusEnum;
import com.hclm.web.enums.ResponseCode;
import com.hclm.web.repository.EmployeesRepository;
import com.hclm.web.repository.StoreRepository;
import com.hclm.web.utils.RandomUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeesMangerService {
    private final EmployeesRepository employeesRepository;
    private final StoreRepository storeRepository;

    /**
     * 添加员工
     *
     * @param request 请求
     * @return {@link EmployeesResponse }
     */
    public EmployeesResponse addEmployees(EmployeesAddRequest request) {
        Employees employees = EmployeesManagerMapper.INSTANCE.toEntity(request);
        employees.setEmployeesId(RandomUtil.generateEmployeesId());
        //设置商户id com.hclm.tenant.TenantInterceptor 无法处理插入 因此这里手动设置商户id
        employees.setMerchantId(MerchantLoginUtil.getMerchantId());
        employees.setCreatedAt(System.currentTimeMillis());
        employeesRepository.save(employees);//保存员工
        return EmployeesManagerMapper.INSTANCE.toResponse(employees);
    }

    /**
     * 获取员工列表
     *
     * @param storeId 门店id     * @return {@link List }
     */
    public List<EmployeesResponse> getEmployeesList(String storeId) {
        return EmployeesManagerMapper.INSTANCE.toResponseList(employeesRepository.findByStoreIdAndNotDeleted(storeId));
    }

    /**
     * 删除员工
     *
     * @param employeesId 员工id
     */
    public void deleteEmployees(String employeesId) {
        int num = employeesRepository.softDelete(employeesId);
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
        Employees employees = employeesRepository.findById(employeesId)
                .orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYEES_NOT_FOUND));
        //转换,赋值,跳过null字段
        EmployeesManagerMapper.INSTANCE.updateEntityFromRequest(employees, request);
        employees.setUpdatedAt(System.currentTimeMillis());
        employeesRepository.save(employees);//更新员工
        return EmployeesManagerMapper.INSTANCE.toResponse(employees);
    }

    /**
     * 获取员工
     *
     * @param employeesId 员工id
     * @return {@link EmployeesResponse }
     */
    public EmployeesResponse getEmployees(String employeesId) {
        Employees employees = employeesRepository.findById(employeesId)
                .orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYEES_NOT_FOUND));
        return EmployeesManagerMapper.INSTANCE.toResponse(employees);
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
    private EmployeesResponse copyEmployeeToStore(String employeeId, EmployeesCopyRequest request) {
        // 1. 查找源员工
        Employees sourceEmployee = employeesRepository.findById(employeeId)
                .orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYEES_NOT_FOUND));

        // 2. 验证员工属于源店铺和当前商家
        if (!sourceEmployee.getStoreId().equals(request.getSourceStoreId())) {
            throw new IllegalArgumentException("员工不属于指定的源店铺或商家");
        }

        // 3. 检查目标店铺是否已存在相同邮箱的员工
        if (sourceEmployee.getEmail() != null) {
            boolean emailExists = employeesRepository.existsByEmailAndStoreIdAndIsDeletedFalse(
                    sourceEmployee.getEmail(), request.getTargetStoreId());
            if (emailExists) {
                throw new IllegalArgumentException("目标店铺已存在相同邮箱的员工");
            }
        }

        // 4. 创建新员工实体
        Employees newEmployee = new Employees();
        newEmployee.setEmployeesId(RandomUtil.generateEmployeesId());
        newEmployee.setMerchantId(MerchantLoginUtil.getMerchantId());
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

        newEmployee.setCreatedAt(System.currentTimeMillis());
        newEmployee.setIsDeleted(false);

        // 6. 保存新员工
        employeesRepository.save(newEmployee);

        return EmployeesManagerMapper.INSTANCE.toResponse(newEmployee);
    }

}
