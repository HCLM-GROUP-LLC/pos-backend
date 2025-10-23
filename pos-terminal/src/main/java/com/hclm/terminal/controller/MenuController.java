package com.hclm.terminal.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 菜单控制器
 *
 * @author hanhua
 * @since 2025/10/23
 */
@Validated
@SaCheckLogin //检查登录
@Tag(name = "菜单")
@RequiredArgsConstructor
@RequestMapping("/menu")
@RestController
public class MenuController {
}
