package com.hclm.terminal.handler;

import com.hclm.tenant.TenantSupplier;
import com.hclm.terminal.utils.EmployeesLoginUtil;
import org.springframework.stereotype.Component;

/**
 * 终端租户供应商
 *
 * @author hanhua
 * @since 2025/10/19
 */
@Component
public class TerminalTenantSupplier implements TenantSupplier {

    @Override
    public String tenantId() {
        return EmployeesLoginUtil.loginCache().getMerchantId();
    }

    @Override
    public String tenantIdColumn() {
        return "merchant_id"; // 数据库字段名
    }


}
