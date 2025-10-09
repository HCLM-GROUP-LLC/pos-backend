package com.hclm.terminal.service;

import com.hclm.satoken.SaTokenUtil;
import com.hclm.terminal.mapper.EmployeesMapper;
import com.hclm.terminal.pojo.request.EmployeesLoginRequest;
import com.hclm.terminal.pojo.response.EmployeesLoginResponse;
import com.hclm.terminal.utils.EmployeesLoginUtil;
import com.hclm.web.BusinessException;
import com.hclm.web.entity.Device;
import com.hclm.web.entity.Employees;
import com.hclm.web.enums.ResponseCode;
import com.hclm.web.repository.EmployeesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EmployeesService {
    private final EmployeesRepository employeesRepository;
    private final DeviceService deviceService;

    /**
     * 登录
     *
     * @param request 请求
     * @return {@link EmployeesLoginResponse }
     */
    public EmployeesLoginResponse login(EmployeesLoginRequest request) {
        Device device = deviceService.findByDeviceId(request.getDeviceId());
        //查找员工信息
        Employees employees = employeesRepository
                .findByPassCodeAndStoreId(request.getPassCode(), device.getStoreId())
                .orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYEES_PASSCODE_NOT_FOUND));
        employees.setLastLoginAt(System.currentTimeMillis());
        employeesRepository.save(employees);//更新登录时间
        //登录
        EmployeesLoginUtil.login(employees.getEmployeesId(), EmployeesMapper.INSTANCE.toLoginCache(employees));
        //转换为登录响应
        return EmployeesLoginUtil.toLoginResponse(employees);
    }
    
    public EmployeesLoginResponse getLoginInfo() {
        //查找员工信息
        Employees employees = employeesRepository
                .findById(SaTokenUtil.getLoginId())
                .orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYEES_NOT_FOUND));
        //转换为登录响应
        return EmployeesLoginUtil.toLoginResponse(employees);
    }
}
