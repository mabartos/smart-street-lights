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
    public String getAccessToken(AuthUserDTO authUserDTO) {
        UserEntity user = Optional.ofNullable(session.getUserRepository().getByUsername(authUserDTO.username))
                .orElseThrow(() -> new ClientErrorException("User does not exist!", Response.Status.UNAUTHORIZED));

        if (!BCrypt.checkPw(authUserDTO.password, user.getPassword())) {
            throw new ClientErrorException("Wrong password", Response.Status.UNAUTHORIZED);
        }

        //TODO add key-location
        return Jwt.subject(user.getUsername())
                .upn(user.getEmail())
                .issuer(user.getFirstName() + " " + user.getLastName())
                .groups(user.getRole())
                .sign();
    }
}
