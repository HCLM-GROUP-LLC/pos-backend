package com.hclm.tenant;

import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.util.List;

@EnableAspectJAutoProxy
@Slf4j
@Configuration
public class TenantConfiguration {

    @Bean
    public TenantAspect tenantAspect() {
        log.info("租户切面开启");
        return new TenantAspect();
    }

    /**
     * 租户行级拦截器
     *
     * @return {@link TenantLineInnerInterceptor }
     */
    @Bean
    public TenantLineInnerInterceptor tenantLineInnerInterceptor(List<TenantSupplier> tenantSupplier) {
        log.info("租户行级拦截器开启");
        return new TenantLineInnerInterceptor(new PosTenantLineHandler(tenantSupplier.get(0)));
    }
}
