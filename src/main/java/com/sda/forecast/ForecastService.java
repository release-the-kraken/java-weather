package com.sda.forecast;

import com.google.gson.Gson;
import com.sda.config.Configuration;
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
    private final Gson gson;
    Forecast getForecast(Long locationId, Integer day){
        LocationDTO location = gson.fromJson(locationController.getLocationById(locationId),
                LocationDTO.class);
        Double longitude = location.getLongitude();
        Double latitude = location.getLatitude();
        String weatherData = getWeatherData(latitude, longitude, day);
        Forecast forecast = gson.fromJson(weatherData, Forecast.class);
        Forecast savedForecast = forecastRepository.save(forecast);
        return savedForecast;
    }
    private String getWeatherData(Double latitude, Double longitude, Integer day){
        URI uri = URI.create("api.openweathermap.org/data/2.5/forecast?lat=%s&lon=%s&units=metric&cnt=%s&appid=%s"
                .formatted(latitude,longitude, day, API_KEY));
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
