package org.smartlights.user.resources.providers;

import io.smallrye.jwt.build.Jwt;
import org.apache.directory.api.ldap.model.password.BCrypt;
import org.smartlights.user.data.AuthUserDTO;
import org.smartlights.user.entity.UserEntity;
import org.smartlights.user.resources.AuthResource;
import org.smartlights.user.resources.UserSession;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

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
    public String getAccessToken(AuthUserDTO authUserDTO) {
        UserEntity user = Optional.ofNullable(session.getUserRepository().getByUsername(authUserDTO.username))
                .orElseThrow(this::badCredentialsException);

        if (!BCrypt.checkPw(authUserDTO.password, user.getPassword())) {
            throw badCredentialsException();
        }

        return Jwt.subject(user.getEmail())
                .upn(user.getUsername())
                .issuer(user.getFirstName() + " " + user.getLastName())
                .groups(user.getRoles())
                .sign();
    }

    private ClientErrorException badCredentialsException() {
        return new ClientErrorException("Invalid username or password", Response.Status.UNAUTHORIZED);
    }
}
