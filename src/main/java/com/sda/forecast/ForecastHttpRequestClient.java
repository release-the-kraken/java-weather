package com.sda.forecast;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.sda.config.Configuration.API_KEY;

public class ForecastHttpRequestClient implements HttpRequestClient{
    public String getWeatherData(Double latitude, Double longitude) {
        try {
            URI uri = URI.create("https://api.openweathermap.org/data/2.5/onecall?lat=%s&lon=%s&units=metric&exclude=current,hourly,minutely&appid=%s"
                    .formatted(latitude, longitude, API_KEY));
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(uri)
                    .build();
            System.out.println(client.send(request, HttpResponse.BodyHandlers.ofString()).body());
            return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
