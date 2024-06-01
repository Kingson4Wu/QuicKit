package com.kxw.quickit.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigUtil.class);

    private ConfigUtil() {}

    public static String getValue(String key, String defaultValue) {
        return defaultValue;
    }

    public static Double getDoubleProperty(String key, double defaultValue) {
        return defaultValue;
    }

    public static String getValue(String namespace, String key, String defaultValue) {
        return defaultValue;
    }

    public static Boolean getBooleanValue(String key, boolean defaultValue) {
        return defaultValue;
    }

    public static Boolean getBooleanValue(String namespace, String key, boolean defaultValue) {
        return defaultValue;
    }

    public static Long getLongValue(String key, long defaultValue) {
        return defaultValue;
    }

    public static Integer getIntValue(String key, int defaultValue) {
        return defaultValue;
    }

    public static String[] getListValue(String key, String... values) {
        return values;
    }
}
