package com.hclm.tenant;

public interface TenantSupplier {
    /**
     * 租户id的值
     *
     * @return {@link String }
     */
    String tenantId();

    /**
     * 租户id列名
     *
     * @return {@link String }
     */
    String tenantIdColumn();

}
