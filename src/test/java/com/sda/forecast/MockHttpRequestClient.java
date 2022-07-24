package com.sda.forecast;

public class MockHttpRequestClient implements HttpRequestClient{
    @Override
    public String getWeatherData(Double latitude, Double longitude) {
        return null;
    }
}
