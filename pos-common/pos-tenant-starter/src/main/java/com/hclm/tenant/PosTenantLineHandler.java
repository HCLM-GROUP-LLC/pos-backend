package com.hclm.tenant;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;

/**
 * pos租户行处理程序
 *
 * @author hanhua
 * @since 2025/10/19
 */
public record PosTenantLineHandler(TenantSupplier tenantSupplier) implements TenantLineHandler {
    @Override
    public Expression getTenantId() {
        return new StringValue(tenantSupplier.tenantId());

    }

    @Override
    public String getTenantIdColumn() {
        return tenantSupplier.tenantIdColumn();
    }

    @Override
    public boolean ignoreTable(String tableName) {
        return !TenantContext.isEnabled();//当前请求不需要租户功能
    }
}
