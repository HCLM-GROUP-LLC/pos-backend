package com.hclm.resource;

/**
 * 上传文件异常
 *
 * @author hanhua
 * @since 2025/10/24
 */
public class UploadFileException extends RuntimeException {
    public UploadFileException(Throwable cause) {
        super("文件上传失败", cause);
    }
}
