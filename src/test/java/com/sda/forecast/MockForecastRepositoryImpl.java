package com.sda.forecast;

import com.sda.location.Location;

public class MockForecastRepositoryImpl implements ForecastRepository {

    @Override
    public Forecast save(Forecast forecast, Location location) {
        forecast.setId(1L);
        return forecast;
    }
}
