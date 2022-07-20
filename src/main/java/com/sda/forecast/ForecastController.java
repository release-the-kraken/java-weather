package com.sda.forecast;

import com.google.gson.Gson;
import com.sda.location.LocationController;
import com.sda.location.LocationDTO;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ForecastController {
    private final ForecastService forecastService;

    private final Gson gson;

    //GET:/forecast?location{id}&date={day}
    //GET:/forecast?location{id}
    String getForecast(Long id, Integer day){
        Forecast forecast = forecastService.getForecast(id, day);
        ForecastDTO response = mapForecastToForecastDTO(forecast);
        return gson.toJson(response);
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
