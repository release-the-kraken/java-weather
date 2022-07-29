package com.sda.forecast;

import com.sda.location.Location;
import lombok.RequiredArgsConstructor;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
@RequiredArgsConstructor
public class MockForecastRepositoryImpl implements ForecastRepository {
    Map<Integer, Forecast> mockDatabase;
private final Clock clock;
    @Override
    public Forecast save(Forecast forecast, Location location) {
        forecast.setId(1L);
        forecast.setLocation(location);
        return forecast;
    }

    @Override
    public Optional<Forecast> getActiveForecastForRequiredLocationAndDay(Long id, Instant requestedForecastDate, Instant requestDate) {
        mockDatabase = populateMockDB(this.clock);
        Forecast forecast = mockDatabase
                .values()
                .stream()
                .filter(f -> filterActiveForecasts(f, requestedForecastDate, requestDate))
                .findFirst()
                .orElse(null);
        return Optional.ofNullable(forecast);
    }

    private boolean filterActiveForecasts(Forecast f, Instant requestedForecastDate, Instant requestDate) {
        ZoneId zoneId = ZoneId.of("Europe/Warsaw");
        ZoneOffset zoneOffset = zoneId.getRules().getOffset(LocalDateTime.now());
        LocalDate forecastDate = LocalDate.ofInstant(f.getForecastDate(), zoneOffset);
        Period period = Period.between(forecastDate, LocalDate.ofInstant(requestedForecastDate, zoneOffset));
        int periodDiff = period.getDays();
        LocalDateTime forecastCreatedDate = LocalDateTime.ofInstant(f.getCreatedDate(), zoneOffset);
        Duration duration = Duration.between(forecastCreatedDate, LocalDateTime.ofInstant(requestDate, zoneOffset));
        long durationDiff = duration.toHours();
        return periodDiff == 0 && durationDiff >= 0 && durationDiff < 24;
    }
    Map<Integer, Forecast> populateMockDB(Clock clock){
        Map<Integer, Forecast> forecasts = new HashMap<>();
        Forecast forecast1 = new Forecast();
        forecast1.setId(1L);
        forecast1.setCreatedDate(clock.instant());
        forecast1.setForecastDate(clock.instant().plus(2, ChronoUnit.DAYS));
        Forecast forecast2 = new Forecast();
        forecast2.setId(2L);
        forecast2.setCreatedDate(clock.instant());
        forecast2.setForecastDate(clock.instant().plus(4, ChronoUnit.DAYS));
        Forecast forecast3 = new Forecast();
        forecast3.setId(3L);
        forecast3.setCreatedDate(clock.instant());
        forecast3.setForecastDate(clock.instant().plus(6, ChronoUnit.DAYS));
        forecasts.put(1, forecast1);
        forecasts.put(2, forecast2);
        forecasts.put(3, forecast3);
        return forecasts;
    }

}
