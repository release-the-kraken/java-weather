package com.sda.forecast;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ForecastClientResponseDTO {
    List<DailyForecastDTO> daily;

    @Data
    public class DailyForecastDTO {
        @JsonProperty("dt")
        long timestamp;
        @JsonProperty("temp")
        TemperatureDTO temperature;
        int humidity;
        int pressure;
        @JsonProperty("wind_speed")
        float windSpeed;
        @JsonProperty("wind_deg")
        int windDirection;

        @Data
        public class TemperatureDTO {
            @JsonProperty("day")
            float daysTemperature;
        }
    }


}
