package com.sda.forecast;

import com.sda.location.Location;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

public class MockForecastRepositoryImpl implements ForecastRepository {

    @Override
    public Forecast save(Forecast forecast, Location location) {
        forecast.setId(1L);
        forecast.setLocation(location);
        return forecast;
    }

    @Override
    public Optional<Forecast> getActiveForecastForRequiredLocationAndDay(Long id, int days) {

        return Optional.empty();
    }

}
