package com.hclm.resource;

import com.hclm.mybatis.enums.FileOwnerEnum;

/**
 * 文件所有者 用于反向关联
 *
 * @author hanhua
 * @since 2025/10/24
 */
public interface FileOwner {
    /**
     * 获取所有者类型
     *
     * @return {@link FileOwnerEnum }
     */
    FileOwnerEnum getOwnerType();

    /**
     * 获取所有者id
     *
     * @return {@link String }
     */
    String getOwnerId();
}
