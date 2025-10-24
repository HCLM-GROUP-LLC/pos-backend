package com.hclm.resource;

import io.minio.MinioClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ResourceConfiguration {
    @Bean
    public MinioProperties minioProperties() {
        return new MinioProperties();
    }

    /**
     * minio客户端
     *
     * @param minioProperties minio属性
     * @return minio客户端
     */
    @Bean
    public MinioClient minioClient(MinioProperties minioProperties) {
        return MinioUtil.createClient(minioProperties);
    }
}
