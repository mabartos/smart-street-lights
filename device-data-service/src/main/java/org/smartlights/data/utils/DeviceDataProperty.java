package org.smartlights.data.utils;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * Available values provided in data
 */
public enum DeviceDataProperty {
    INTENSITY("intensity", Byte::valueOf),
    AMBIENT("ambient", Integer::valueOf),
    DETECT("detect", Boolean::valueOf);

    private final String key;
    private final Function<String, ?> function;

    DeviceDataProperty(String key, Function<String, ?> function) {
        this.key = key;
        this.function = function;
    }

    /**
     * Get key of the field
     */
    public String getKey() {
        return key;
    }

    /**
     * Get converting function
     */
    public Function<String, ?> convert() {
        return function;
    }

    /**
     * Check if value is available
     */
    public static boolean containsProperty(String name) {
        return Arrays.stream(DeviceDataProperty.values()).
                anyMatch(f -> f.getKey().equals(name));
    }

    /**
     * Convert value to type determined by Key
     */
    public static Object convertToTypeByKey(Map.Entry<String, String> entry) {
        return convertToTypeByKey(entry.getKey(), entry.getValue());
    }

    /**
     * Convert value to type determined by Key
     */
    public static Object convertToTypeByKey(String key, String value) {
        return Arrays.stream(DeviceDataProperty.values())
                .filter(f -> f.getKey().equals(key))
                .map(f -> {
                    try {
                        return f.function.apply(value.trim());
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Cannot convert values"));
    }
}
