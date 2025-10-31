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
    EMAIL_ALREADY_EXISTS(10008, "email.already.exists"),
    /**
     * 未找到设备id
     */
    DEVICE_ID_NOT_FOUND(10005, "deviceid.notFound"),
    EMPLOYEES_NOT_FOUND(10006, "employees.notFound"),
    /**
     * 员工重复登录
     */
    EMPLOYEES_DUPLICATE_LOGINS(10007, "employees.duplicateLogins"),
    /**
     * 未找到菜单
     */
    MENU_NOT_FOUND(10009, "menu.notFound"),
    MENU_NAME_ALREADY_EXISTS(10010, "menu.name.already.exists"),
    /**
     * 菜单类别名称重复
     */
    MENU_CATEGORY_NAME_ALREADY_EXISTS(10011, "menu.category.name.already.exists"),
    /**
     * 未找到菜单类别
     */
    MENU_CATEGORY_NOT_FOUND(10012, "menu.category.notFound"),
    /**
     * 菜单项名称已存在
     */
    MENU_ITEM_NAME_ALREADY_EXISTS(10013, "menu.item.name.already.exists"),
    /**
     * 分类项目重复
     */
    CAT_ITEMS_REPEAT(10014, "menu.cat.items.repeat"),
    /**
     * 电话号码不存在
     */
    PHONE_NUMBER_NOT_EXIST(10015, "phone.number.not.exist"),
    /**
     * 短信发送过于频繁
     */
    SMS_SEND_TOO_FREQUENT(10016, "sms.send.too.frequent"),
    /**
     * 短信代码过期
     */
    SMS_CODE_EXPIRE(10017, "sms.code.expire"),
    /**
     * 电话号码不匹配
     */
    PHONE_NUMBER_NOT_MATCH(10018, "phone.number.not.match"),
    MERCHANT_NOT_FOUND(10019, "merchant.not.found");
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
