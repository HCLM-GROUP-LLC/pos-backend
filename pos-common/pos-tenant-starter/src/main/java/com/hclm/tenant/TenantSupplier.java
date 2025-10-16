package com.hclm.tenant;

@FunctionalInterface
public interface TenantSupplier {
    String tenantId();
}
