package org.smartlights.user.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashSet;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"id", "email"})
public class UserDTO extends AuthUserDTO {
    public Long id;
    public String firstName;
    public String lastName;
    public String email;
    public Set<String> roles = new HashSet<>();
}
