package org.smartlights.user.resources.providers;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.smallrye.jwt.build.Jwt;
import org.smartlights.user.data.AuthUserDTO;
import org.smartlights.user.entity.UserEntity;
import org.smartlights.user.resources.AuthResource;
import org.smartlights.user.resources.UserSession;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Optional;

@Transactional
@Path("auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class AuthResourceProvider implements AuthResource {

    @Inject
    UserSession session;

    @Override
    public String getToken(AuthUserDTO authUserDTO) {
        UserEntity user = Optional.ofNullable(session.getUserRepository().getByUsername(authUserDTO.username))
                .orElseThrow(() -> new ForbiddenException("User does not exist!"));

        if (!user.getPassword().equals(BcryptUtil.bcryptHash(authUserDTO.password))) {
            throw new ForbiddenException("Wrong password");
        }

        //TODO better approach
        return Jwt.subject(user.getUsername()).groups(user.getRole()).sign();
    }
}
