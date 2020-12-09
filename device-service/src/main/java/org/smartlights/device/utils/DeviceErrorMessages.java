package org.smartlights.device.utils;

import javax.ws.rs.NotFoundException;
import java.util.function.Supplier;

public class DeviceErrorMessages {

    public static Supplier<NotFoundException> notFoundException() {
        return notFoundException(null);
    }

    public static Supplier<NotFoundException> notFoundException(Long id) {
        return () -> new NotFoundException(getNotFoundMessage(id));
    }

    public static String getNotFoundMessage() {
        return getNotFoundMessage(null);
    }

    public static String getNotFoundMessage(Long id) {
        return id != null ? "Device with id: " + id + " not found!" : "Device not found!";
    }

}
