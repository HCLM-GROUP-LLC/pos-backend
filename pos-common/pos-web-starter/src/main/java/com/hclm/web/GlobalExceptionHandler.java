package com.hclm.web;

import cn.dev33.satoken.exception.NotLoginException;
import com.hclm.web.enums.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public ApiResponse<Void> handleBusinessException(BusinessException businessException) {
        return ApiResponse.error(businessException.getCode(), businessException.getMessage());
    }

    /**
     * 处理绑定异常
     */
    @ExceptionHandler(BindException.class)
    public ApiResponse<Map<String, String>> handleBindException(BindException bindException) {
        if (bindException instanceof MethodArgumentNotValidException argumentNotValidException) {
            log.error("参数校验异常: {}", argumentNotValidException.getMessage());
        } else {
            log.error("绑定异常: {}", bindException.getMessage());
        }
        Map<String, String> errors = bindException
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .filter(fieldError -> fieldError.getDefaultMessage() != null)
                .collect(Collectors.toMap(FieldError::getField, DefaultMessageSourceResolvable::getDefaultMessage));
        return ApiResponse.build(ResponseCode.BAD_REQUEST.getCode(), ResponseCode.BAD_REQUEST.getMessage(), errors);
    }

    /**
     * 处理未登录异常
     *
     * @param ex ex
     * @return {@link ApiResponse }
     */
    @ExceptionHandler(NotLoginException.class)
    public ApiResponse<Void> handleNotLoginException(NotLoginException ex) {
        log.error("处理未登录异常: {}", ex.getMessage());

        return ApiResponse.error(ResponseCode.UNAUTHORIZED.getCode(), ex.getMessage());
    }

    /**
     * 处理所有其他异常
     */
    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> handleGlobalException(
            Exception ex, WebRequest request) {
        log.error("系统异常: {}", ex.getMessage(), ex);
        return ApiResponse.error(ResponseCode.INTERNAL_SERVER_ERROR.getCode(), ex.getMessage());
    }
}
