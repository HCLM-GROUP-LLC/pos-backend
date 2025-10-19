package com.hclm.tenant;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Slf4j
@Aspect
public class TenantAspect {
    @Before("@within(tenant)")
    public void beforeTenantMethodExecution(Tenant tenant) {
        TenantContext.enable();
    }
}
