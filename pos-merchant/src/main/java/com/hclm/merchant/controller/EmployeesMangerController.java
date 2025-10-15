package com.hclm.merchant.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.hclm.merchant.pojo.request.EmployeesAddRequest;
import com.hclm.merchant.pojo.response.EmployeesResponse;
import com.hclm.merchant.service.EmployeesMangerService;
import com.hclm.web.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 员工管理控制器
 *
 * @author hanhua
 * @since 2025/10/14
 */
@Validated
@SaCheckLogin //检查登录
@Tag(name = "员工管理")
@RequiredArgsConstructor
@RequestMapping("/employees")
@RestController
public class EmployeesMangerController {
    private final EmployeesMangerService employeesMangerService;

    @Operation(summary = "添加员工")
    @PostMapping("")
    public ApiResponse<EmployeesResponse> addEmployees(@Valid @RequestBody EmployeesAddRequest request) {
        return ApiResponse.success(employeesMangerService.addEmployees(request));
    }
}
