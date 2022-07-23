package com.sda.forecast;

import com.sda.location.Location;

import java.util.Optional;

public interface ForecastRepository {
    Forecast save(Forecast forecast, Optional<Location> location);
    Optional<Forecast> getLastForecastForLocation(Long id);
}
