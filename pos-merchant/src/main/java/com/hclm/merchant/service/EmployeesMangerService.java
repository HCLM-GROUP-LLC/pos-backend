package com.hclm.merchant.service;

import com.hclm.merchant.mapper.EmployeesManagerMapper;
import com.hclm.merchant.pojo.request.EmployeesAddRequest;
import com.hclm.merchant.pojo.request.EmployeesUpdateRequest;
import com.hclm.merchant.pojo.response.EmployeesResponse;
import com.hclm.merchant.utils.MerchantLoginUtil;
import com.hclm.web.BusinessException;
import com.hclm.web.entity.Employees;
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
        //设置商户id
        employees.setMerchantId(MerchantLoginUtil.getMerchantId());
        employees.setCreatedAt(System.currentTimeMillis());
        employees.setMerchantId(MerchantLoginUtil.getMerchantId());
        employeesRepository.save(employees);//保存员工
        return EmployeesManagerMapper.INSTANCE.toResponse(employees);
    }

    /**
     * 获取员工列表
     *
     * @param storeId 门店id     * @return {@link List }
     */
    public List<EmployeesResponse> getEmployeesList(String storeId) {
        String merchantId = MerchantLoginUtil.getMerchantId();
        boolean hasPermission = storeRepository.existsByIdAndMerchantId(storeId, merchantId);
        if (!hasPermission) {
            throw new IllegalArgumentException("no permission access storeId:"+storeId);
        }

        List<Employees> employeesList = employeesRepository.findByStoreIdAndMerchantIdAndNotDeleted(storeId, merchantId);
        return EmployeesManagerMapper.INSTANCE.toResponseList(employeesList);
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
}
