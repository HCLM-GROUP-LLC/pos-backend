package com.hclm.tenant;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 租户注解 用于开启租户功能
 *
 * @author hanhua
 * @since 2025/10/16
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Tenant {
    /**
     * 值 租户id字段名
     *
     * @return {@link String }
     */
    String value() default "merchant_id";
}
