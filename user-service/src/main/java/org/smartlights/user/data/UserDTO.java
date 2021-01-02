package org.smartlights.user.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO extends AuthUserDTO {
    public Long id;
    public String firstName;
    public String lastName;
    public String email;
    public String role;
}
