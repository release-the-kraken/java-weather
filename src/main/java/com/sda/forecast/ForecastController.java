package com.sda.forecast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.sda.location.LocationController;
import com.sda.location.LocationDTO;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ForecastController {
    private final ForecastService forecastService;

    private final ObjectMapper objectMapper;

    //GET:/forecast?location{id}&date={day}
    //GET:/forecast?location{id}
    public String getForecast(Long id, Integer day) throws JsonProcessingException {
        //TODO id / day validation
        Forecast forecast = null;//TODO zmienić na metodę findById z locationRepository
        try {
            forecast = forecastService.getForecast(id, day);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        ForecastDTO response = mapForecastToForecastDTO(forecast);
        return objectMapper.writeValueAsString(response);
    }
    private ForecastDTO mapForecastToForecastDTO(Forecast forecast){
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
