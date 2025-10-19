package com.hclm.terminal.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hclm.terminal.converter.EmployeesConverter;
import com.hclm.terminal.pojo.cache.EmployeesLoginCache;
import com.hclm.terminal.pojo.request.EmployeesLoginRequest;
import com.hclm.terminal.pojo.response.EmployeesLoginResponse;
import com.hclm.terminal.service.DeviceService;
import com.hclm.terminal.service.EmployeesAttendanceService;
import com.hclm.terminal.service.EmployeesService;
import com.hclm.terminal.utils.EmployeesLoginUtil;
import com.hclm.web.BusinessException;
import com.hclm.web.constant.BusinessConstant;
import com.hclm.web.entity.Device;
import com.hclm.web.entity.Employees;
import com.hclm.web.entity.EmployeesAttendance;
import com.hclm.web.enums.ResponseCode;
import com.hclm.web.mapper.EmployeesMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmployeesServiceImpl extends ServiceImpl<EmployeesMapper, Employees> implements EmployeesService {
    private final DeviceService deviceService;
    private final EmployeesAttendanceService employeesAttendanceService;

    /**
     * 登录
     *
     * @param request 请求
     * @return {@link EmployeesLoginResponse }
     */
    @Override
    public EmployeesLoginResponse login(EmployeesLoginRequest request) {
        Device device = deviceService.getById(request.getDeviceId());
        //查找员工信息
        Employees employees = lambdaQuery()
                .eq(Employees::getStoreId, device.getStoreId())
                .eq(Employees::getPassCode, request.getPassCode())
                .oneOpt()
                .orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYEES_PASSCODE_NOT_FOUND));
        //检查是否重复登录
        if (EmployeesLoginUtil.isLogin(employees.getEmployeesId())) {
            throw new BusinessException(ResponseCode.EMPLOYEES_DUPLICATE_LOGINS);
        }
        long curTime = System.currentTimeMillis();
        //检查24小时之内重复登录
        if (curTime - employees.getLastLoginAt() < BusinessConstant.DUPLICATE_LOGINS_TIMEOUT) {
            throw new BusinessException(ResponseCode.EMPLOYEES_DUPLICATE_LOGINS);
        }
        employees.setLastLoginAt(curTime);
        updateById(employees);//更新登录时间
        //插入考勤记录
        EmployeesAttendance employeesAttendance = new EmployeesAttendance();
        employeesAttendance.setEmployeesId(employees.getEmployeesId());
        employeesAttendance.setStoreId(device.getStoreId());
        employeesAttendance.setMerchantId(device.getMerchantId());//商家 ID
        employeesAttendance.setClockInTime(curTime);
        employeesAttendanceService.save(employeesAttendance);
        //缓存登录信息
        EmployeesLoginCache loginCache = EmployeesConverter.INSTANCE.toLoginCache(employees);
        loginCache.setAttendanceId(employeesAttendance.getAttendanceId());//记录考勤ID，退出登录时更新clock out时间
        //登录
        EmployeesLoginUtil.login(employees.getEmployeesId(), loginCache);
        //转换为登录响应
        return EmployeesLoginUtil.toLoginResponse(employees);
    }

    /**
     * 获取登录信息
     *
     * @return {@link EmployeesLoginResponse }
     */
    @Override
    public EmployeesLoginResponse getLoginInfo() {
        //查找员工信息
        Employees employees = getOptById(EmployeesLoginUtil.getLoginId())
                .orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYEES_NOT_FOUND));
        //转换为登录响应
        return EmployeesLoginUtil.toLoginResponse(employees);
    }

    @Override
    public void logout() {
        //获取登录信息
        EmployeesLoginCache loginCache = EmployeesLoginUtil.loginCache();
        if (loginCache.getAttendanceId() == null) {
            EmployeesLoginUtil.logout();
            return;//先不考虑考勤不存在的情况
        }
        //更新考勤信息
        EmployeesAttendance employeesAttendance = employeesAttendanceService
                .getOptById(loginCache.getAttendanceId())
                .orElse(null);
        if (employeesAttendance == null) {
            EmployeesLoginUtil.logout();
            return;//先不考虑考勤不存在的情况
        }
        long curTime = System.currentTimeMillis();
        employeesAttendance.setClockOutTime(curTime);
        employeesAttendance.setTotalTime(curTime - employeesAttendance.getClockInTime());
        //保存考勤信息
        employeesAttendanceService.save(employeesAttendance);
        //登出
        EmployeesLoginUtil.logout();
    }
}
