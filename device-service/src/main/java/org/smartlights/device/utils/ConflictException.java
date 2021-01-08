package org.smartlights.device.utils;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.Response;

/**
 * Conflict exception
 */
public class ConflictException extends ClientErrorException {
    public ConflictException() {
        super(Response.Status.CONFLICT);
    }

    public ConflictException(String message) {
        super(message, Response.Status.CONFLICT);
    }
}
