package org.smartlights.simulation.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.HashSet;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO extends AuthUserDTO {
    public Long id;
    public String firstName;
    public String lastName;
    public String email;
    public Set<String> roles = new HashSet<>();
}
