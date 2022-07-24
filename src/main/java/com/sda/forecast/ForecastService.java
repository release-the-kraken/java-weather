package com.sda.forecast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.sda.location.*;
import lombok.RequiredArgsConstructor;

import java.time.*;
import java.util.Optional;

@RequiredArgsConstructor
public class ForecastService {

    private ZoneId zoneId;
    private ZoneOffset zoneOffset;
    private final ForecastRepository hibernateForecastRepository;
    private final LocationService locationService;
    private final LocationRepository locationRepository;
    private final ForecastHttpRequestClient httpRequestClient;
    private final ObjectMapper objectMapper;

    Forecast getActiveForecast(Long locationId, Integer day) throws JsonProcessingException {

        LocationDTO locationDTO = locationService.getByID(locationId);
        Optional<Location> locationOptional = locationRepository.findById(locationId);
        Location location = locationOptional
                .orElseThrow(() -> new IllegalArgumentException("No location with id %s".formatted(locationId)));
        Double longitude = locationDTO.getLongitude();
        Double latitude = locationDTO.getLatitude();
        Long id = locationDTO.getId();

        Optional<Forecast> forecastOptional = hibernateForecastRepository.getLastForecastForLocation(id, day);

        return forecastOptional
                .orElseGet(() -> getForecastAndSave(day, location, longitude, latitude));
    }

    private Forecast getForecastAndSave(Integer day, Location location, Double longitude, Double latitude) {
        try {
            String weatherData = httpRequestClient.getWeatherData(latitude, longitude);
            ForecastClientResponseDTO responseDTO = objectMapper
                    .readValue(weatherData, ForecastClientResponseDTO.class);
            SingleForecast singleForecast = mapToSingleForecast(responseDTO, day);
            Forecast forecast = mapToForecastEntity(singleForecast);
            return hibernateForecastRepository.save(forecast, location);
        } catch (JsonProcessingException e) {
            System.out.println("Error message: %s".formatted(e.getMessage()));
            throw new RuntimeException(e);
        }
    }

    private SingleForecast mapToSingleForecast(ForecastClientResponseDTO responseDTO, Integer day) {
        ForecastClientResponseDTO.DailyForecastDTO forecastForDay = responseDTO.getDaily().get(day);
        SingleForecast single = new SingleForecast();
        single.setTemperature(forecastForDay.getTemperature().getDaysTemperature());
        single.setHumidity(forecastForDay.getHumidity());
        single.setPressure(forecastForDay.getPressure());
        single.setWindSpeed(forecastForDay.getWindSpeed());
        single.setWindDirection(forecastForDay.getWindDirection());
        single.setTimestamp(forecastForDay.getTimestamp());
        return single;
    }

    private Forecast mapToForecastEntity(SingleForecast single) {
        Forecast forecast = new Forecast();
        forecast.setTemperature(single.getTemperature());
        forecast.setHumidity(single.getHumidity());
        forecast.setPressure(single.getPressure());
        forecast.setWindSpeed(single.getWindSpeed());
        forecast.setWindDirection(single.getWindDirection());
        forecast.setForecastDate(Instant.ofEpochSecond(single.getTimestamp()));
        zoneId = ZoneId.of("Europe/Warsaw");
        zoneOffset = zoneId.getRules().getOffset(LocalDateTime.now());
        forecast.setCreatedDate(LocalDateTime.now().toInstant(zoneOffset));
        return forecast;
    }


}
