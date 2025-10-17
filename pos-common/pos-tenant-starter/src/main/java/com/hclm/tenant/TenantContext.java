package com.hclm.tenant;

public class TenantContext {
    private static final ThreadLocal<String> CURRENT_TENANT = new ThreadLocal<>();
    private static final ThreadLocal<String> FIELD_NAME = new ThreadLocal<>();
    private static final ThreadLocal<Boolean> IS_ENABLED = new ThreadLocal<>();

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

    public static void enable() {
        IS_ENABLED.set(true);
    }

    public static void disable() {
        IS_ENABLED.set(false);
    }

    public static boolean isEnabled() {
        return IS_ENABLED.get() != null && IS_ENABLED.get();
    }
}
