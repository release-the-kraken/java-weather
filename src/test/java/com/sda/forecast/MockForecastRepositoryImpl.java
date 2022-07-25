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
        LocalDateTime forecastCreatedDate = LocalDateTime
                .of(2022, 7, 24, 12, 0);
        LocalDateTime userRequiredDate = LocalDateTime
                .of(2022, 7, 24, 12, 0)
                .plusDays(days);
        Forecast forecast1 = new Forecast();
        forecast1.setId(1L);
        forecast1.setCreatedDate(forecastCreatedDate.toInstant(ZoneOffset.UTC));
        forecast1.setForecastDate(userRequiredDate.toInstant(ZoneOffset.UTC));
        Map<Long, Forecast> mockDatabase = new HashMap<>();
        mockDatabase.put(forecast1.getId(), forecast1);
        Forecast forecastFromMockDB = mockDatabase.values()
                .stream()
                .filter(forecast -> filterActiveForecasts(forecastCreatedDate, forecast))
                .findFirst()
                .orElse(null);
        return Optional.ofNullable(forecastFromMockDB);
    }
    private boolean filterActiveForecasts(LocalDateTime forecastCreatedDate, Forecast forecast) {
        Duration duration = Duration
                .between(forecastCreatedDate, forecast.getCreatedDate());
        long diff = duration.toHours();
        return diff < 24 && !duration.isNegative();
    }
}
