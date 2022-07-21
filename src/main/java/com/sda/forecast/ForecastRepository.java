package com.sda.forecast;

import com.sda.location.Location;

public interface ForecastRepository {
    Forecast save(Forecast forecast, Location location);

}
