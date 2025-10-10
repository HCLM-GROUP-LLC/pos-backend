package com.hclm.web.utils;

import com.alibaba.fastjson2.JSONObject;

public class JsonUtil {
    @SuppressWarnings("unchecked")
    public static <T> T convertObject(Object jsonObject, Class<T> clazz) {
        if (jsonObject instanceof JSONObject x) {
            return x.to(clazz);
        }
        if (jsonObject.getClass().isAssignableFrom(clazz)) {
            return (T) jsonObject;
        }
        return null;
    }
}
