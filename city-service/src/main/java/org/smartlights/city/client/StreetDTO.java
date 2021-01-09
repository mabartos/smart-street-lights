package org.smartlights.city.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.smartlights.city.utils.StreetCategory;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StreetDTO implements Serializable {
    public Long streetId;
    public String cityName;
    public Long cityId;
    public StreetCategory streetCategory;
}
