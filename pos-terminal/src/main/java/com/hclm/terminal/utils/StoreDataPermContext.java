package com.hclm.terminal.utils;

public class StoreDataPermContext {
    private static final ThreadLocal<Boolean> IS_ENABLED = new ThreadLocal<>();

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
