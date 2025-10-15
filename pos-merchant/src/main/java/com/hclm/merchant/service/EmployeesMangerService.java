package com.hclm.merchant.service;

import com.hclm.merchant.mapper.EmployeesManagerMapper;
import com.hclm.merchant.pojo.request.EmployeesAddRequest;
import com.hclm.merchant.pojo.response.EmployeesResponse;
import com.hclm.merchant.utils.MerchantLoginUtil;
import com.hclm.web.entity.Employees;
import com.hclm.web.repository.EmployeesRepository;
import com.hclm.web.utils.RandomUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeesMangerService {
    private final EmployeesRepository employeesRepository;

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
        return EmployeesManagerMapper.INSTANCE.toResponse(employees);
    }
}
