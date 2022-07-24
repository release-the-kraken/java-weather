package com.sda.forecast;

import com.sda.location.Location;

import java.time.Instant;
import java.util.Optional;

public class MockForecastRepositoryImpl implements ForecastRepository {

    @Override
    public Forecast save(Forecast forecast, Location location) {
        forecast.setId(1L);
        return forecast;
    }

    @Override
    public Optional<Forecast> getLastForecastForLocation(Long id, int days) {
        return Optional.empty();
    }
}
