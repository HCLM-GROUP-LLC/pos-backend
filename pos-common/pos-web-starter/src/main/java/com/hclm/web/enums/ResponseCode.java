package com.hclm.web.enums;

import com.hclm.web.utils.MessageUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 错误代码 统一定义业务错误代码
 * 参考 {@link org.springframework.http.HttpStatus}
 *
 * @author hanhua
 * @since 2025/08/28
 */
@Getter
@AllArgsConstructor
public enum ResponseCode {
    /**
     * 成功
     */
    SUCCESS(200, "success"),
    BAD_REQUEST(400, "badrequest"),
    /**
     * 未经授权
     */
    UNAUTHORIZED(401, "unauthorized"),
    /**
     * 未找到
     */
    NOT_FOUND(404, "notfound"),
    INTERNAL_SERVER_ERROR(500, "internal.server.error"),
    //以下开始是业务错误代码
    /**
     * 电子邮件密码错误
     */
    EMAIL_PASSWORD_ERROR(10001, "email.password.error"),
    MERCHANT_DISABLED(10002, "merchant.disabled"),
    DEVICECODE_NOTFOUND(10003, "devicecode.notFound"),
    EMPLOYEES_PASSCODE_NOT_FOUND(10004, "employees.passcode.notFound"),
    /**
     * 未找到设备id
     */
    DEVICE_ID_NOT_FOUND(10005, "deviceid.notFound"),
    EMPLOYEES_NOT_FOUND(10006, "employees.notFound");
    /**
     * 代码
     */
    private final int code;
    /**
     * 消息代码 关联资源文件
     */
    private final String messageCode;

    /**
     * 获取消息
     *
     * @return {@link String }
     */
    public String getMessage() {
        return MessageUtil.message(messageCode);
    }

    /**
     * 获取消息
     *
     * @return {@link String }
     */
    public String getMessage(Object... params) {
        return MessageUtil.message(messageCode, params);
    }
}
