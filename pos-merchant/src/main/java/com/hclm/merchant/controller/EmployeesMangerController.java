package com.hclm.merchant.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.hclm.merchant.pojo.request.EmployeesAddRequest;
import com.hclm.merchant.pojo.request.EmployeesUpdateRequest;
import com.hclm.merchant.pojo.response.EmployeesResponse;
import com.hclm.merchant.service.EmployeesMangerService;
import com.hclm.web.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @Operation(summary = "获取员工列表")
    @GetMapping("")
    public ApiResponse<List<EmployeesResponse>> getEmployeesList(@Schema(description = "门店id") @RequestParam(required = true) String storeId) {
        return ApiResponse.success(employeesMangerService.getEmployeesList(storeId));
    }

    @Operation(summary = "删除员工")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteEmployees(@Schema(description = "员工id") @PathVariable String id) {
        employeesMangerService.deleteEmployees(id);
        return ApiResponse.success();
    }

    @Operation(summary = "更新员工")
    @PutMapping("/{id}")
    public ApiResponse<EmployeesResponse> updateEmployees(@Schema(description = "员工id") @PathVariable String id, @Valid @RequestBody EmployeesUpdateRequest request) {
        return ApiResponse.success(employeesMangerService.updateEmployees(id, request));
    }

    @Operation(summary = "获取某个员工")
    @GetMapping("/{id}")
    public ApiResponse<EmployeesResponse> getEmployees(@Schema(description = "员工id") @PathVariable String id) {
        return ApiResponse.success(employeesMangerService.getEmployees(id));
    }
}
