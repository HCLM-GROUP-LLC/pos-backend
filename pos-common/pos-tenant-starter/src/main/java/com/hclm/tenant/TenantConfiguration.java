package com.hclm.tenant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.lang.Nullable;

import java.util.Map;

@EnableAspectJAutoProxy
@Slf4j
@ConditionalOnBean(TenantSupplier.class)
@Configuration
public class TenantConfiguration implements HibernatePropertiesCustomizer {

    @Bean
    public TenantAspect tenantAspect(@Nullable TenantSupplier tenantSupplier) {
        log.info("租户切面开启");
        return new TenantAspect(tenantSupplier);
    }

    @Bean
    public TenantInterceptor tenantInterceptor() {
        return new TenantInterceptor();
    }

    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        hibernateProperties.put("hibernate.session_factory.statement_inspector", "com.hclm.tenant.TenantInterceptor");
    }
}
