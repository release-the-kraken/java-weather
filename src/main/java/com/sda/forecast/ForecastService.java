package com.sda.forecast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.sda.location.Location;
import com.sda.location.LocationController;
import com.sda.location.LocationDTO;
import lombok.RequiredArgsConstructor;

import java.time.*;
import java.util.Optional;

@RequiredArgsConstructor
public class ForecastService {
    private final ForecastRepository forecastRepository;
    private final LocationController locationController;
    private final ForecastHttpRequestClient httpRequestClient;
    private final ObjectMapper objectMapper;

    Forecast getForecast(Long locationId, Integer day) throws JsonProcessingException {
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
        String city = location.getCity();
        //GET LAST FORECAST FOR LOCATION FROM DATABASE
        //QUERY --> "SELECT f FROM Forecast f LEFT JOIN FETCH l.forecasts WHERE city=:city ORDER BY createdDate DESC LIMIT 1"
        //check if forecast exists
        //IF NOT fetch forecast from openweather
        //check if forecastDate is after LocalDateTime.now()
        //IF NOT get weather data from openweather
        //ELSE get createdDate from queried forecast
        //check if Period between created date and LocalDate.now() is < 24h
        //IF IS return queried forecast
        //ELSE fetch forecast from openweather
        Optional<Forecast> forecastOptional = forecastRepository.getLastForecastForLocation(city);
        String weatherData = "";
        if(forecastOptional.isEmpty()){
            weatherData = httpRequestClient.getWeatherData(latitude, longitude);
        }else {
            LocalDate forecastDate = LocalDate.ofInstant(forecastOptional.get().getCreatedDate(), ZoneOffset.ofHours(2));
            LocalDate currentDate = LocalDate.now();
            Period period = Period.between(currentDate, forecastDate);
            int diff = Math.abs(period.getDays());
            if(forecastDate.isBefore(currentDate)){
                weatherData = httpRequestClient.getWeatherData(latitude, longitude);
            }else if(diff < 24){
                return forecastOptional.get();
            }else{
                weatherData = httpRequestClient.getWeatherData(latitude, longitude);
            }
        }

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
