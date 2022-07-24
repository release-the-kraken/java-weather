package com.sda.forecast;

public interface HttpRequestClient {
    String getWeatherData(Double latitude, Double longitude);
}
