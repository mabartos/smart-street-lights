package org.smartlights.user.data;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.smartlights.user.entity.UserEntity;

public class UserSerializer {

    private static ObjectMapper getObjectMapper() {
        return new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    public static UserEntity modelToEntity(UserDTO userDTO) {
        return getObjectMapper().convertValue(userDTO, UserEntity.class);
    }

    public static UserDTO entityToModel(UserEntity userEntity) {
        return getObjectMapper().convertValue(userEntity, UserDTO.class);
    }
}
