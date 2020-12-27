package org.smartlights.street.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.smartlights.street.utils.StreetCategory;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StreetDTO implements Serializable {
    public Long streetId;
    public String cityName;
    public Long cityId;
    public StreetCategory streetCategory;
}
