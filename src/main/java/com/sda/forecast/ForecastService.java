package com.sda.forecast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.sda.location.*;
import lombok.RequiredArgsConstructor;

import java.time.*;
import java.util.Optional;

@RequiredArgsConstructor
public class ForecastService {

    private final ForecastRepository hibernateForecastRepository;
    private final LocationService locationService;
    private final LocationRepository locationRepository;
    private final ForecastHttpRequestClient httpRequestClient;
    private final ObjectMapper objectMapper;

    Forecast getActiveForecast(Long locationId, Integer day) throws JsonProcessingException {

        LocationDTO locationDTO = locationService.getByID(locationId);
        Optional<Location> location = locationRepository.findById(locationId);
        Double longitude = locationDTO.getLongitude();
        Double latitude = locationDTO.getLatitude();
        Long id = locationDTO.getId();

        Optional<Forecast> forecastOptional = hibernateForecastRepository.getLastForecastForLocation(id);

        String weatherData = "";
        if (forecastOptional.isEmpty()) {
            weatherData = httpRequestClient.getWeatherData(latitude, longitude);
            ForecastClientResponseDTO responseDTO = objectMapper.readValue(weatherData, ForecastClientResponseDTO.class);
            SingleForecast singleForecast = mapToSingleForecast(responseDTO, day);
            Forecast forecast = mapToForecastEntity(singleForecast);
            return hibernateForecastRepository.save(forecast, location);
        } else {
            LocalDate forecastDate = LocalDate.ofInstant(forecastOptional.get().getCreatedDate(), ZoneOffset.ofHours(2));
            LocalDate currentDate = LocalDate.now();
            Period period = Period.between(currentDate, forecastDate);
            int diff = Math.abs(period.getDays());
            if (forecastDate.isBefore(currentDate)) {
                weatherData = httpRequestClient.getWeatherData(latitude, longitude);
                ForecastClientResponseDTO responseDTO = objectMapper.readValue(weatherData, ForecastClientResponseDTO.class);
                SingleForecast singleForecast = mapToSingleForecast(responseDTO, day);
                Forecast forecast = mapToForecastEntity(singleForecast);
                return hibernateForecastRepository.save(forecast, location);
            } else if (diff < 24) {
                return forecastOptional.get();
            } else {
                weatherData = httpRequestClient.getWeatherData(latitude, longitude);
                ForecastClientResponseDTO responseDTO = objectMapper.readValue(weatherData, ForecastClientResponseDTO.class);
                SingleForecast singleForecast = mapToSingleForecast(responseDTO, day);
                Forecast forecast = mapToForecastEntity(singleForecast);
                return hibernateForecastRepository.save(forecast, location);
            }
        }

//        return hibernateForecastRepository.getLastForecastForLocation(id)
//                .filter(this::filterActiveForecast)
//                .orElseGet(() -> getForecastAndSave(day, location, longitude, latitude));
    }

    private boolean filterActiveForecast(Forecast forecast) {
        LocalDate forecastDate = LocalDate.ofInstant(forecast.getCreatedDate(), ZoneOffset.ofHours(2));
        LocalDate currentDate = LocalDate.now();
        Period period = Period.between(currentDate, forecastDate);
        int diff = Math.abs(period.getDays());

        if (forecastDate.isBefore(currentDate)) {
            return false;
        } else if (diff < 24) {
            return true;
        } else {
            return false;
        }
    }

    private Forecast getForecastAndSave(Integer day, Optional<Location> location, Double longitude, Double latitude) {
        try {
            String weatherData = httpRequestClient.getWeatherData(latitude, longitude);
            ForecastClientResponseDTO responseDTO = objectMapper.readValue(weatherData, ForecastClientResponseDTO.class);
            SingleForecast singleForecast = mapToSingleForecast(responseDTO, day);
            Forecast newForecast = mapToForecastEntity(singleForecast);
            return hibernateForecastRepository.save(newForecast, location);
        } catch (JsonProcessingException e) {
            // todo
            throw new RuntimeException();
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
        ZoneId zoneId = ZoneId.of("Europe/Warsaw");
        ZoneOffset zoneOffset = zoneId.getRules().getOffset(LocalDateTime.now());
        forecast.setCreatedDate(LocalDateTime.now().toInstant(zoneOffset));
        return forecast;
    }


}
