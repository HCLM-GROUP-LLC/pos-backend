package com.hclm.web;

import com.hclm.web.enums.ResponseCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(ResponseCode responseCode, Object... params) {
        this(responseCode.getCode(), responseCode.getMessage(params));
    }
}
