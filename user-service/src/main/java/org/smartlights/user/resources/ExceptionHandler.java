package org.smartlights.user.resources;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Optional;
import java.util.logging.Logger;

@Provider
public class ExceptionHandler implements ExceptionMapper<Throwable> {

    private static final Logger logger = Logger.getLogger(ExceptionHandler.class.getName());

    @Override
    public Response toResponse(Throwable exception) {
        Response response;
        logger.warning(Optional.ofNullable(exception.getCause()).map(Throwable::toString).orElseGet(exception::getMessage));
        if (isWebApplicationException(exception)) {
            WebApplicationException webEx = getWebApplicationException(exception);
            response = Response.status(webEx.getResponse().getStatus())
                    .entity(webEx.getMessage())
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        } else {
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("500 Internal error")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
        return response;
    }

    private boolean isWebApplicationException(Throwable exception) {
        return exception instanceof WebApplicationException || exception.getCause() instanceof WebApplicationException;
    }

    private WebApplicationException getWebApplicationException(Throwable exception) {
        return (WebApplicationException) Optional.ofNullable(exception.getCause()).orElse(exception);
    }
}
