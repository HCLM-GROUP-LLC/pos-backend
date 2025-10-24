package com.hclm.resource.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "file")
@Data
public class FileProperties {
    @Data
    public static class MinioProperties {
        /**
         * 端点 ip/域名
         */
        private String host;
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
    }

    /**
     * 预览地址的域名,可以为空,提前nginx配置好
     */
    private String previewHost;
    /**
     * 默认的桶
     */
    private String minioDefaultBucket;
    /**
     * minio
     */
    private MinioProperties minio;
    /**
     * 租户配额 单位是字节
     */
    private Long tenantQuota;
}
