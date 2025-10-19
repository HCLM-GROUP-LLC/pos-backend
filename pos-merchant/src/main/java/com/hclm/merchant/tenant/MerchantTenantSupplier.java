package com.hclm.merchant.tenant;

import com.hclm.merchant.utils.MerchantLoginUtil;
import com.hclm.tenant.TenantSupplier;
import org.springframework.stereotype.Component;

@Component
public class MerchantTenantSupplier implements TenantSupplier {

    @Override
    public String tenantId() {
        return MerchantLoginUtil.getMerchantId();
    }

    @Override
    public String tenantIdColumn() {
        return "merchant_id";//商户id作为数据库的租户字段，行级隔离
    }

}
