package org.smartlights.user.data;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.smartlights.user.entity.UserEntity;

import javax.ws.rs.BadRequestException;
import java.util.Optional;

/**
 * User serializer
 */
public class UserSerializer {

    private static ObjectMapper getObjectMapper() {
        return new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    /**
     * Model to Entity
     *
     * @param userDTO
     * @return
     */
    public static UserEntity modelToEntity(UserDTO userDTO) {
        Optional.ofNullable(userDTO).orElseThrow(() -> new BadRequestException("You have to provide body!"));
        return getObjectMapper().convertValue(userDTO, UserEntity.class);
    }

    /**
     * Entity to Model
     *
     * @param userEntity
     * @return
     */
    public static UserDTO entityToModel(UserEntity userEntity) {
        return getObjectMapper().convertValue(userEntity, UserDTO.class);
    }
}
