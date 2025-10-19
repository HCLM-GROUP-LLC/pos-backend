package com.hclm.terminal.aspect;

import com.hclm.terminal.annotation.StoreDataPerm;
import com.hclm.terminal.utils.StoreDataPermContext;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class StoreDataPermAspect {
    @Before("@within(dataPermission)")
    public void beforeTenantMethodExecution(StoreDataPerm dataPermission) {
        StoreDataPermContext.enable();
    }
}
