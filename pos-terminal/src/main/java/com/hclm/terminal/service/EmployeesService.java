package com.hclm.terminal.service;

import com.hclm.terminal.mapper.EmployeesMapper;
import com.hclm.terminal.pojo.cache.EmployeesLoginCache;
import com.hclm.terminal.pojo.request.EmployeesLoginRequest;
import com.hclm.terminal.pojo.response.EmployeesLoginResponse;
import com.hclm.terminal.utils.EmployeesLoginUtil;
import com.hclm.web.BusinessException;
import com.hclm.web.constant.BusinessConstant;
import com.hclm.web.entity.Device;
import com.hclm.web.entity.Employees;
import com.hclm.web.entity.EmployeesAttendance;
import com.hclm.web.enums.ResponseCode;
import com.hclm.web.repository.EmployeesAttendanceRepository;
import com.hclm.web.repository.EmployeesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class EmployeesService {
    private final EmployeesRepository employeesRepository;
    private final DeviceService deviceService;
    private final EmployeesAttendanceRepository employeesAttendanceRepository;

    /**
     * 登录
     *
     * @param request 请求
     * @return {@link EmployeesLoginResponse }
     */
    @Transactional
    public EmployeesLoginResponse login(EmployeesLoginRequest request) {
        Device device = deviceService.findByDeviceId(request.getDeviceId());
        //查找员工信息
        Employees employees = employeesRepository
                .findByPassCodeAndStoreId(request.getPassCode(), device.getStoreId())
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
        employeesRepository.save(employees);//更新登录时间
        //插入考勤记录
        EmployeesAttendance employeesAttendance = new EmployeesAttendance();
        employeesAttendance.setEmployeesId(employees.getEmployeesId());
        employeesAttendance.setStoreId(device.getStoreId());
        employeesAttendance.setClockInTime(curTime);
        employeesAttendanceRepository.save(employeesAttendance);
        //缓存登录信息
        EmployeesLoginCache loginCache = EmployeesMapper.INSTANCE.toLoginCache(employees);
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
    public EmployeesLoginResponse getLoginInfo() {
        //查找员工信息
        Employees employees = employeesRepository
                .findById(EmployeesLoginUtil.getLoginId())
                .orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYEES_NOT_FOUND));
        //转换为登录响应
        return EmployeesLoginUtil.toLoginResponse(employees);
    }

    public void logout() {
        //获取登录信息
        EmployeesLoginCache loginCache = EmployeesLoginUtil.loginCache();
        if (loginCache.getAttendanceId() == null) {
            EmployeesLoginUtil.logout();
            return;//先不考虑考勤不存在的情况
        }
        //更新考勤信息
        EmployeesAttendance employeesAttendance = employeesAttendanceRepository
                .findById(loginCache.getAttendanceId())
                .orElse(null);
        if (employeesAttendance == null) {
            EmployeesLoginUtil.logout();
            return;//先不考虑考勤不存在的情况
        }
        long curTime = System.currentTimeMillis();
        employeesAttendance.setClockOutTime(curTime);
        employeesAttendance.setTotalTime(curTime - employeesAttendance.getClockInTime());
        //保存考勤信息
        employeesAttendanceRepository.save(employeesAttendance);
        //登出
        EmployeesLoginUtil.logout();
    }
}
