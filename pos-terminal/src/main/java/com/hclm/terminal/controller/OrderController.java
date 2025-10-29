package com.hclm.terminal.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.hclm.terminal.pojo.request.OrderCreateRequest;
import com.hclm.terminal.service.OrderService;
import com.hclm.web.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@SaCheckLogin //检查登录
@Tag(name = "订单")
@RequiredArgsConstructor
@RequestMapping("/orders")
@RestController
public class OrderController {
    private final OrderService orderService;

    @Operation(summary = "创建订单")
    @RequestMapping("/create")
    public ApiResponse<Void> create(@Valid OrderCreateRequest request) {
        orderService.create(request);
        return ApiResponse.success();
    }
}
