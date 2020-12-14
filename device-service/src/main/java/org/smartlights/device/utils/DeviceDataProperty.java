package org.smartlights.device.utils;

public enum DeviceDataProperty {
    INTENSITY("intensity", Byte.class),
    AMBIENT("ambient", Integer.class),
    DETECT("detect", Boolean.class);

    private String name;
    private Class<?> clazz;

    DeviceDataProperty(String name, Class<?> clazz) {
        this.name = name;
        this.clazz = clazz;
    }

    public String getName() {
        return name;
    }

    public Class<?> getClazz() {
        return clazz;
    }
}
