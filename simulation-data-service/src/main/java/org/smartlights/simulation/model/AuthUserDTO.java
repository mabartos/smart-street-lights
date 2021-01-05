package org.smartlights.simulation.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthUserDTO {
    @NotNull
    public String username;
    @NotNull
    public String password;
}