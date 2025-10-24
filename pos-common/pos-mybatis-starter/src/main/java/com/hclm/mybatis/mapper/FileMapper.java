package com.hclm.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hclm.mybatis.entity.FileEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FileMapper extends BaseMapper<FileEntity> {
    /**
     * 获取已使用存储空间
     *
     * @param merchantId 商户id
     * @return long
     */
    long getUsedStorage(@Param("merchantId") String merchantId);
}
