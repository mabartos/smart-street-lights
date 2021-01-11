package org.smartlights.user.resources.providers;

import io.smallrye.jwt.build.Jwt;
import org.apache.directory.api.ldap.model.password.BCrypt;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.smartlights.user.data.AuthUserDTO;
import org.smartlights.user.entity.UserEntity;
import org.smartlights.user.resources.AuthResource;
import org.smartlights.user.resources.UserSession;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

import static org.smartlights.user.utils.Constants.FIRSTNAME;
import static org.smartlights.user.utils.Constants.LASTNAME;

@Transactional
@Path("auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class AuthResourceProvider implements AuthResource {

    @Inject
    UserSession session;

    @POST
    @Path("token")
    @Produces(MediaType.TEXT_PLAIN)
    @Counted(name = "countAccessToken", description = "How many access token has been released")
    @Timed(name = "accessTokenReleaseTime", description = "A measure of how long it takes to provide token to the user.")
    @CircuitBreaker
    @Retry(maxRetries = 5)
    @Timeout
    public String getAccessToken(AuthUserDTO authUserDTO) {
        authUserDTO = Optional.ofNullable(authUserDTO).orElseThrow(() -> new BadRequestException("You have to provide auth body."));

        UserEntity user = Optional.ofNullable(session.getUserRepository().getByUsername(authUserDTO.username))
                .orElseThrow(this::badCredentialsException);

        if (!BCrypt.checkPw(authUserDTO.password, user.getPassword())) {
            throw badCredentialsException();
        }

        return Jwt.subject(user.getEmail())
                .upn(user.getUsername())
                .groups(user.getRoles())
                .claim(FIRSTNAME, user.getFirstName())
                .claim(LASTNAME, user.getLastName())
                .sign();
    }

    private ClientErrorException badCredentialsException() {
        return new ClientErrorException("Invalid username or password", Response.Status.UNAUTHORIZED);
    }
}
