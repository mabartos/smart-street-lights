package org.smartlights.user.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"username", "password"})
public class AuthUserDTO {
    @NotNull
    public String username;
    @NotNull
    public String password;
}
