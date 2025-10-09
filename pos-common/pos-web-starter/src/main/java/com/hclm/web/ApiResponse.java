package com.hclm.web;

import com.hclm.web.enums.ResponseCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

/**
 * api响应 定义json结构
 *
 * @author hanhua
 * @since 2025/10/06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    /**
     * 响应状态码
     */
    @Schema(description = "响应状态码")
    private int code;

    /**
     * 响应消息
     */
    @Schema(description = "响应消息")
    private String message;

    /**
     * 响应数据
     */
    @Schema(description = "响应数据")
    private T data;
    /**
     * 响应时间戳
     */
    @Schema(description = "响应时间戳,毫秒级")
    private Long timestamp;

    /**
     * 是成功
     *
     * @return boolean
     */
    @Schema(description = "是否成功")
    public boolean isSuccess() {
        return ResponseCode.SUCCESS.getCode() == code;
    }

    /**
     * 成功
     *
     * @param data 数据
     * @param <T>  data字段的类型
     * @return {@link ApiResponse } 响应
     */
    public static <T> ApiResponse<T> success(@Nullable T data) {
        return build(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), data);
    }

    /**
     * 成功
     *
     * @param <T> data字段的类型
     * @return {@link ApiResponse } 响应
     */
    public static <T> ApiResponse<T> success() {
        return success(null);
    }

    /**
     * 构建错误响应
     *
     * @param code    代码
     * @param message 信息
     * @return {@link ApiResponse }<{@link T }>
     */
    public static <T> ApiResponse<T> error(int code, String message) {
        return build(code, message, null);
    }

    /**
     * 构建响应
     *
     * @param code    代码
     * @param message 信息
     * @param data    数据
     * @return {@link ApiResponse }<{@link T }>
     */
    public static <T> ApiResponse<T> build(int code, String message, T data) {
        return new ApiResponse<>(code, message, data, System.currentTimeMillis());
    }
}
