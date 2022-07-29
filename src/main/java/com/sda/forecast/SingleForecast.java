package com.sda.forecast;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
@Data
public class SingleForecast {
    @JsonProperty("dt")
    long timestamp;
    @JsonProperty("temp")
    float temperature;
    int humidity;
    int pressure;
    @JsonProperty("wind_speed")
    float windSpeed;
    @JsonProperty("wind_deg")
    int windDirection;

}
