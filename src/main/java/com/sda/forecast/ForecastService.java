package com.sda.forecast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.sda.config.Configuration;
import com.sda.location.Location;
import com.sda.location.LocationController;
import com.sda.location.LocationDTO;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


import static com.sda.config.Configuration.API_KEY;

@RequiredArgsConstructor
public class ForecastService {
    private final ForecastRepository forecastRepository;
    private final LocationController locationController;
    private final ObjectMapper objectMapper;

    Forecast getForecast(Long locationId, Integer day) throws JsonProcessingException {//getActiveForecast
        LocationDTO locationDTO = objectMapper.readValue(locationController.getLocationById(locationId), LocationDTO.class);
        Location location = objectMapper.readValue(locationController.getLocationById(locationId), Location.class);
        Double longitude = locationDTO.getLongitude();
        Double latitude = locationDTO.getLatitude();
        String weatherData = getWeatherData(latitude, longitude, day);
        Forecast forecast = objectMapper.readValue(weatherData, Forecast.class);//<--------
        Forecast savedForecast = forecastRepository.save(forecast, location);
        return savedForecast;
    }

    private String getWeatherData(Double latitude, Double longitude, Integer day) {
        URI uri = URI.create("https://api.openweathermap.org/data/2.5/forecast?lat=%s&lon=%s&units=metric&cnt=%s&appid=%s"
                .formatted(latitude, longitude, day, API_KEY));
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();
        HttpResponse<String> response = null;

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return response.body();
    }

}
