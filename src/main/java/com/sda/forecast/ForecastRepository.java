package com.sda.forecast;

import com.sda.location.Location;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;

public interface ForecastRepository {
    Forecast save(Forecast forecast, Location location);
    Optional<Forecast> getActiveForecastForRequiredLocationAndDay(Long id, Instant requestedForecastDate, Instant requestDate);
}
