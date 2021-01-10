package org.smartlights.simulation;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.net.ConnectException;
import java.util.logging.Logger;

@Provider
public class ExceptionHandler implements ExceptionMapper<ConnectException> {
    private static final Logger log = Logger.getLogger("Remote service handler");

    @Override
    public Response toResponse(ConnectException exception) {
        log.warning("Remote service is unavailable.");
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
}
