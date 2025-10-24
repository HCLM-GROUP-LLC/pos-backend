package com.hclm.resource;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "minio", ignoreInvalidFields = true)
@Data
public class MinioProperties {
    /**
     * 端点 ip/域名
     */
    private String endpoint;
    /**
     * 端口
     */
    private int port;
    /**
     * 安全
     */
    private boolean secure;
    /**
     * 访问键
     */
    private String accessKey;
    /**
     * 秘钥键
     */
    private String secretKey;
    /**
     * 默认的桶
     */
    private String bucket;
    /**
     * 预览地址的域名,可以为空,提前nginx配置好
     */
    private String previewHost;
}
