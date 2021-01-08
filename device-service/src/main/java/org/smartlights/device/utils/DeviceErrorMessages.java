package org.smartlights.device.utils;

import javax.ws.rs.NotFoundException;
import java.util.function.Supplier;

/**
 * Helper class for error messages
 */
public class DeviceErrorMessages {

    /**
     * Get not found supplier
     *
     * @return
     */
    public static Supplier<NotFoundException> notFoundException() {
        return notFoundException(null);
    }

    /**
     * Get not found supplier with ID
     *
     * @return
     */
    public static Supplier<NotFoundException> notFoundException(Long id) {
        return () -> new NotFoundException(getNotFoundMessage(id));
    }

    /**
     * Get not found message
     *
     * @return
     */
    public static String getNotFoundMessage() {
        return getNotFoundMessage(null);
    }

    /**
     * Get not found message with ID
     *
     * @return
     */
    public static String getNotFoundMessage(Long id) {
        return id != null ? "Device with id: " + id + " not found!" : "Device not found!";
    }

}
