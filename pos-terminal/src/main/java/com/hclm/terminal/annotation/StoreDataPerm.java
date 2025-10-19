package com.hclm.terminal.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * 注解控制开启门店数据权限
 *
 * @author hanhua
 * @since 2025/10/19
 */
@Target({ElementType.TYPE})
public @interface StoreDataPerm {
}
