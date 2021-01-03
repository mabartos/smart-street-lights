package org.smartlights.device.resources;

import org.hibernate.exception.ConstraintViolationException;

import javax.persistence.PersistenceException;
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
        try {
            logger.warning(Optional.ofNullable(exception.getCause()).map(Throwable::toString).orElseGet(exception::getMessage));

            if (isWebApplicationException(exception)) {
                WebApplicationException webEx = getWebApplicationException(exception);
                return Response.status(webEx.getResponse().getStatus())
                        .entity(webEx.getMessage())
                        .type(MediaType.TEXT_PLAIN)
                        .build();
            } else if (exception instanceof PersistenceException) {
                if (isDuplicate(exception.getCause())) {
                    return Response.status(Response.Status.CONFLICT).build();
                }
                PersistenceException cve = (PersistenceException) exception;
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(Optional.ofNullable(cve.getCause())
                                .map(Throwable::getMessage)
                                .orElseGet(cve::getMessage))
                        .build();
            }
        } catch (Exception e) {
            logger.warning(e.getMessage());
            return getInternalErrorResponse();
        }
        return getInternalErrorResponse();
    }

    private Response getInternalErrorResponse() {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("500 Internal error")
                .type(MediaType.TEXT_PLAIN)
                .build();
    }

    // WebApplicationException
    private boolean isWebApplicationException(Throwable exception) {
        return exception instanceof WebApplicationException || exception.getCause() instanceof WebApplicationException;
    }

    private WebApplicationException getWebApplicationException(Throwable exception) {
        return (WebApplicationException) Optional.ofNullable(exception.getCause()).orElse(exception);
    }

    // Duplicate
    private boolean isDuplicate(Throwable exception) {
        return exception instanceof ConstraintViolationException && ((ConstraintViolationException) exception).getSQLException().getMessage().contains("duplicate");
    }
}
