package com.hclm.resource;

import com.hclm.resource.properties.FileProperties;
import com.hclm.resource.service.FileMinioServiceImpl;
import com.hclm.resource.service.FileService;
import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class ResourceConfiguration {

    @Bean
    public FileProperties fileProperties() {
        return new FileProperties();
    }

    /**
     * minio客户端
     *
     * @param fileProperties 属性
     * @return minio客户端
     */
    @Bean
    public MinioClient minioClient(FileProperties fileProperties) {
        log.info("minio配置:{}", fileProperties);
        FileProperties.MinioProperties minioProperties = fileProperties.getMinio();
        return MinioClient.builder()
                .endpoint(minioProperties.getHost(), minioProperties.getPort(), minioProperties.isSecure())
                .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                .build();
    }

    @Bean
    public MinioUtil minioUtil(MinioClient minioClient, FileProperties fileProperties) {
        return new MinioUtil(minioClient, fileProperties.getMinioDefaultBucket(), fileProperties.getPreviewHost());
    }

    @Bean
    public FileService fileService(FileProperties fileProperties, MinioUtil minioUtil) {
        return new FileMinioServiceImpl(fileProperties, minioUtil);
    }
}
