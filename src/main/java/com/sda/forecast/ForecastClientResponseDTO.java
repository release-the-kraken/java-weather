package com.sda.forecast;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class ForecastClientResponseDTO {

    List<DailyForecastDTO> daily;

    @NoArgsConstructor
    @Data
    public static class DailyForecastDTO {
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

        @NoArgsConstructor
        @Data
        public class TemperatureDTO {
            @JsonProperty("day")
            float daysTemperature;
        }
    }


}
