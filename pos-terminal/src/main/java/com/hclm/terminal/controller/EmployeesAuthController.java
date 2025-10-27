package com.hclm.terminal.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import com.hclm.terminal.pojo.request.EmployeesLoginRequest;
import com.hclm.terminal.pojo.response.EmployeesLoginResponse;
import com.hclm.terminal.service.EmployeesService;
import com.hclm.terminal.utils.EmployeesLoginUtil;
import com.hclm.web.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 员工控制器
 *
 * @author hanhua
 * @since 2025/10/09
 */
@Validated
@SaIgnore //不检查登录
@Tag(name = "员工")
@RequiredArgsConstructor
@RequestMapping("/employees-auth")
@RestController
public class EmployeesAuthController {
    private final EmployeesService employeesService;

    /**
     * 登录 clock in
     *
     * @param request 请求
     * @return {@link ApiResponse }<{@link EmployeesLoginResponse }>
     */
    @Operation(summary = "员工登录")
    @PostMapping("/login")
    public ApiResponse<EmployeesLoginResponse> login(@Valid @RequestBody EmployeesLoginRequest request) {
        return ApiResponse.success(employeesService.login(request));
    }

    @Operation(summary = "员工退出登录")
    @PostMapping("/logout")
    public ApiResponse<Void> logout() {
        EmployeesLoginUtil.checkLogin();//检查登录,未登录时抛出异常
        employeesService.logout();
        return ApiResponse.success();
    }

    @Operation(summary = "获取登录信息")
    @GetMapping("/")
    public ApiResponse<EmployeesLoginResponse> getLoginInfo() {
        EmployeesLoginUtil.checkLogin();//检查登录,未登录时抛出异常
        return ApiResponse.success(employeesService.getLoginInfo());
    }
}
