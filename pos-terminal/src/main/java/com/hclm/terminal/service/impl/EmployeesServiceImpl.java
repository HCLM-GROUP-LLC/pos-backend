package com.hclm.terminal.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hclm.mybatis.entity.DeviceEntity;
import com.hclm.mybatis.entity.EmployeeAttendanceEntity;
import com.hclm.mybatis.entity.EmployeeEntity;
import com.hclm.mybatis.mapper.EmployeeMapper;
import com.hclm.terminal.converter.EmployeesConverter;
import com.hclm.terminal.pojo.cache.EmployeesLoginCache;
import com.hclm.terminal.pojo.request.EmployeesLoginRequest;
import com.hclm.terminal.pojo.response.EmployeesLoginResponse;
import com.hclm.terminal.service.DeviceService;
import com.hclm.terminal.service.EmployeesAttendanceService;
import com.hclm.terminal.service.EmployeesService;
import com.hclm.terminal.utils.EmployeesLoginUtil;
import com.hclm.terminal.utils.StoreContext;
import com.hclm.web.BusinessException;
import com.hclm.web.constant.BusinessConstant;
import com.hclm.web.enums.ResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmployeesServiceImpl extends ServiceImpl<EmployeeMapper, EmployeeEntity> implements EmployeesService {
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
        DeviceEntity deviceEntity = deviceService.getById(StoreContext.getDeviceId());
        //查找员工信息
        EmployeeEntity employeeEntity = lambdaQuery()
                .eq(EmployeeEntity::getStoreId, deviceEntity.getStoreId())
                .eq(EmployeeEntity::getPassCode, request.getPassCode())
                .oneOpt()
                .orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYEES_PASSCODE_NOT_FOUND));
        //检查是否重复登录
        if (EmployeesLoginUtil.isLogin(employeeEntity.getEmployeesId())) {
            throw new BusinessException(ResponseCode.EMPLOYEES_DUPLICATE_LOGINS);
        }
        long curTime = System.currentTimeMillis();
        //检查24小时之内重复登录
        if (curTime - employeeEntity.getLastLoginAt() < BusinessConstant.DUPLICATE_LOGINS_TIMEOUT) {
            throw new BusinessException(ResponseCode.EMPLOYEES_DUPLICATE_LOGINS);
        }
        employeeEntity.setLastLoginAt(curTime);
        updateById(employeeEntity);//更新登录时间
        //插入考勤记录
        EmployeeAttendanceEntity employeeAttendanceEntity = new EmployeeAttendanceEntity();
        employeeAttendanceEntity.setEmployeesId(employeeEntity.getEmployeesId());
        employeeAttendanceEntity.setStoreId(deviceEntity.getStoreId());
        employeeAttendanceEntity.setMerchantId(deviceEntity.getMerchantId());//商家 ID
        employeeAttendanceEntity.setClockInTime(curTime);
        employeesAttendanceService.save(employeeAttendanceEntity);
        //缓存登录信息
        EmployeesLoginCache loginCache = EmployeesConverter.INSTANCE.toLoginCache(employeeEntity);
        loginCache.setAttendanceId(employeeAttendanceEntity.getAttendanceId());//记录考勤ID，退出登录时更新clock out时间
        //登录
        EmployeesLoginUtil.login(employeeEntity.getEmployeesId(), loginCache);
        //转换为登录响应
        return EmployeesLoginUtil.toLoginResponse(employeeEntity);
    }

    /**
     * 获取登录信息
     *
     * @return {@link EmployeesLoginResponse }
     */
    @Override
    public EmployeesLoginResponse getLoginInfo() {
        //查找员工信息
        EmployeeEntity employeeEntity = getOptById(EmployeesLoginUtil.getLoginId())
                .orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYEES_NOT_FOUND));
        //转换为登录响应
        return EmployeesLoginUtil.toLoginResponse(employeeEntity);
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
        EmployeeAttendanceEntity employeeAttendanceEntity = employeesAttendanceService
                .getOptById(loginCache.getAttendanceId())
                .orElse(null);
        if (employeeAttendanceEntity == null) {
            EmployeesLoginUtil.logout();
            return;//先不考虑考勤不存在的情况
        }
        long curTime = System.currentTimeMillis();
        employeeAttendanceEntity.setClockOutTime(curTime);
        employeeAttendanceEntity.setTotalTime(curTime - employeeAttendanceEntity.getClockInTime());
        //保存考勤信息
        employeesAttendanceService.save(employeeAttendanceEntity);
        //登出
        EmployeesLoginUtil.logout();
    }
}
