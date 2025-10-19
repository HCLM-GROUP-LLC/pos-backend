package com.hclm.tenant;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Slf4j
@Aspect
public record TenantAspect(TenantSupplier tenantSupplier) {
    @Before("@within(tenant)")
    public void beforeTenantMethodExecution(Tenant tenant) {
        if (tenantSupplier == null) {
            return;
        }
        log.info("租户切面 执行 {}", tenantSupplier.getClass().getName());
        TenantContext.setCurrentTenant(tenantSupplier.tenantId());
        TenantContext.setFieldName(tenant.value());
        TenantContext.enable();
    }
}
