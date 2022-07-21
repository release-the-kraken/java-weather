package com.sda.forecast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.sda.location.Location;
import com.sda.location.LocationController;
import com.sda.location.LocationDTO;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@RequiredArgsConstructor
public class ForecastService {
    private final ForecastRepository forecastRepository;
    private final LocationController locationController;
    private final ForecastHttpRequestClient httpRequestClient;
    private final ObjectMapper objectMapper;

    Forecast getActiveForecast(Long locationId, Integer day) throws JsonProcessingException {
        if (day <= 0 && day > 7) {
            throw new IllegalArgumentException("Day must be in 1 - 7 range");
        }
        if (day == null) {
            day = 1;
        }

        LocationDTO locationDTO = objectMapper.readValue(locationController.getLocationById(locationId), LocationDTO.class);
        Location location = objectMapper.readValue(locationController.getLocationById(locationId), Location.class);
        Double longitude = locationDTO.getLongitude();
        Double latitude = locationDTO.getLatitude();
        String weatherData = httpRequestClient.getWeatherData(latitude, longitude);
        ForecastClientResponseDTO responseDTO = objectMapper.readValue(weatherData, ForecastClientResponseDTO.class);
        Forecast forecast = mapToForecastEntity(responseDTO, day);
        Forecast savedForecast = forecastRepository.save(forecast, location);
        return savedForecast;
    }

    private Forecast mapToForecastEntity(ForecastClientResponseDTO responseDTO, Integer day) {
        ForecastClientResponseDTO.DailyForecastDTO forecastForDay = responseDTO.getDaily().get(day);
        Forecast forecast = new Forecast();
        forecast.setTemperature(forecastForDay.getTemperature().getDaysTemperature());
        forecast.setHumidity(forecastForDay.getHumidity());
        forecast.setPressure(forecastForDay.getPressure());
        forecast.setWindSpeed(forecastForDay.getWindSpeed());
        forecast.setWindDirection(forecastForDay.getWindDirection());
        forecast.setForecastDate(Instant.ofEpochSecond(forecastForDay.getTimestamp()));
        forecast.setCreatedDate(LocalDateTime.now().toInstant(ZoneOffset.ofHours(2)));
        return forecast;
    }


}
