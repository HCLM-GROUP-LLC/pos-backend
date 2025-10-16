package com.hclm.tenant;

public class TenantContext {
    private static final ThreadLocal<String> CURRENT_TENANT = new ThreadLocal<>();
    private static final ThreadLocal<String> FIELD_NAME = new ThreadLocal<>();

    public static void setCurrentTenant(String tenant) {
        CURRENT_TENANT.set(tenant);
    }

    public static String getCurrentTenant() {
        return CURRENT_TENANT.get();
    }

    public static void setFieldName(String fieldName) {
        FIELD_NAME.set(fieldName);
    }

    public static String getFieldName() {
        return FIELD_NAME.get();
    }
}
