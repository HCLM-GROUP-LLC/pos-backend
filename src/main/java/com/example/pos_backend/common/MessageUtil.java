package com.example.pos_backend.common;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

@RequiredArgsConstructor
public class MessageUtil {
    private static MessageSource messageSource;

    public MessageUtil(MessageSource msgSource) {
        messageSource = msgSource;
    }
    

    public static String getMessage(String code, Object... args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}
