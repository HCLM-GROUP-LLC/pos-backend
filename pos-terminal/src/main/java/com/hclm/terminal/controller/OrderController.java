package com.hclm.terminal.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import io.swagger.v3.oas.annotations.tags.Tag;
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
}
