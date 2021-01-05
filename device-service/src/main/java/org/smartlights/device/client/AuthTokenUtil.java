package org.smartlights.device.client;

import org.eclipse.microprofile.jwt.JsonWebToken;
import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;
import java.util.Optional;

/**
 * Provide token to request header
 */
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthTokenUtil implements ContainerRequestFilter {
    private static final String CLASS_PATH = "org.smartlights.device.client";
    private static final String METHOD_NAME = "addAuthorizationHeader";

    public static final String AUTHORIZATION_FIELD = "Authorization";
    public static final String AUTHORIZATION_METHOD_PATH = "{" + CLASS_PATH + "." + METHOD_NAME + "}";

    private static String tokenRaw;

    /**
     * Add authorization header to request to remote service
     */
    public static String addAuthorizationHeader() {
        return "Bearer " + Optional.ofNullable(tokenRaw)
                .orElseThrow(() -> new RuntimeException("You have to provide valid token!!"));
    }

    @Inject
    JsonWebToken jwt;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        tokenRaw = Optional.ofNullable(jwt.getRawToken()).orElse(null);
    }
}