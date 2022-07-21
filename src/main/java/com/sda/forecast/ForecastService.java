package com.sda.forecast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.sda.location.Location;
import com.sda.location.LocationController;
import com.sda.location.LocationDTO;
import com.sun.xml.bind.v2.runtime.unmarshaller.XsiNilLoader;
import lombok.RequiredArgsConstructor;

import java.time.*;
import java.util.Optional;

@RequiredArgsConstructor
public class ForecastService {
    private final HibernateForecastRepositoryImpl hibernateForecastRepository;
    private final LocationController locationController;
    private final ForecastHttpRequestClient httpRequestClient;
    private final ObjectMapper objectMapper;

    Forecast getActiveForecast(Long locationId, Integer day) throws JsonProcessingException {
        LocationDTO locationDTO = objectMapper.readValue(locationController.getLocationById(locationId), LocationDTO.class);
        Location location = objectMapper.readValue(locationController.getLocationById(locationId), Location.class);
        Double longitude = locationDTO.getLongitude();
        Double latitude = locationDTO.getLatitude();
        Long id = locationDTO.getId();
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
        Optional<Forecast> forecastOptional = hibernateForecastRepository.getLastForecastForLocation(id);
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

        SingleForecast singleForecast = mapToSingleForecast(responseDTO, day);
        Forecast forecast = mapToForecastEntity(singleForecast);
        Forecast savedForecast = hibernateForecastRepository.save(forecast, location);

        return savedForecast;
    }

    private SingleForecast mapToSingleForecast(ForecastClientResponseDTO responseDTO, Integer day) {
        ForecastClientResponseDTO.DailyForecastDTO forecastForDay = responseDTO.getDaily().get(day);
        SingleForecast single = new SingleForecast();
        single.getTemperature().setDaysTemperature(forecastForDay.getTemperature().getDaysTemperature());
        single.setHumidity(forecastForDay.getHumidity());
        single.setPressure(forecastForDay.getPressure());
        single.setWindSpeed(forecastForDay.getWindSpeed());
        single.setWindDirection(forecastForDay.getWindDirection());
        single.setTimestamp(forecastForDay.getTimestamp());
        return single;
    }
    private Forecast mapToForecastEntity(SingleForecast single) {
        Forecast forecast = new Forecast();
        forecast.setTemperature(single.getTemperature().getDaysTemperature());
        forecast.setHumidity(single.getHumidity());
        forecast.setPressure(single.getPressure());
        forecast.setWindSpeed(single.getWindSpeed());
        forecast.setWindDirection(single.getWindDirection());
        forecast.setForecastDate(Instant.ofEpochSecond(single.getTimestamp()));
        forecast.setCreatedDate(LocalDateTime.now().toInstant(ZoneOffset.ofHours(2)));
        return forecast;
    }


}
