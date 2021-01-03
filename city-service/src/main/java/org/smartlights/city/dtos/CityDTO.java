package org.smartlights.city.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CityDTO implements Serializable {
    public Long cityId;
    public String cityName;
}
