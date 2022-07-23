package com.sda.forecast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ForecastController {

    private final ForecastService forecastService;
    private final ObjectMapper objectMapper;

    //GET:/forecast?location{id}&date={day}
    //GET:/forecast?location{id}
    public String getForecast(Long id, Integer day) throws JsonProcessingException {
        if (day == null) {
            day = 1;
        }
        if (day <= 0 && day > 7) {
            throw new IllegalArgumentException("Day must be in 1 - 7 range");
        }

        try {
            Forecast forecast = forecastService.getActiveForecast(id, day);
            ForecastDTO response = mapForecastToForecastDTO(forecast);
            return objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            return "{\"error_message\":\"%s\"}".formatted(e.getMessage());
        }
    }

    private ForecastDTO mapForecastToForecastDTO(Forecast forecast) {
        String windDirection = WindDirection
                .getWindDirectionSymbol(forecast.getWindDirection())
                .toString();
        return ForecastDTO.builder()
                .id(forecast.getId())
                .temperature(forecast.getTemperature())
                .humidity(forecast.getHumidity())
                .windSpeed(forecast.getWindSpeed())
                .windDirection(windDirection)
                .pressure(forecast.getPressure())
                .build();
    }
}
