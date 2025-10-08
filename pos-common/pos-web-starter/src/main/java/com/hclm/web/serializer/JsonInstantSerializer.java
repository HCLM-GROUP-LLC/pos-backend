package com.hclm.web.serializer;

import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;

import java.time.format.DateTimeFormatter;

/**
 * Instant序列化
 *
 * @author hanhua
 * @since 2025/10/08
 */
public class JsonInstantSerializer extends InstantSerializer {


    public JsonInstantSerializer() {
        super(InstantSerializer.INSTANCE, true, true, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
    }
}
