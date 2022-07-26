package com.sda.forecast;

import com.sda.location.Location;
import com.sda.utils.HibernateUtils;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ForecastRepositoryTest {
    Forecast forecast;
    Location location;
    MockForecastRepositoryImpl mockForecastRepository;
    private final SessionFactory sessionFactory = HibernateUtils.Instance.getSessionFactory();
    LocalDateTime NOW = LocalDateTime.of(2022, 7, 26, 12, 30, 30 );
    Clock clock;

    @BeforeEach
    void setUp() {
        forecast = new Forecast();
        location = new Location();
        ZoneId zoneId = ZoneId.of("Europe/Warsaw");
        ZoneOffset zoneOffset = zoneId.getRules().getOffset(LocalDateTime.now());
        clock = Clock.fixed(NOW.toInstant(zoneOffset), zoneId);
        mockForecastRepository = new MockForecastRepositoryImpl(clock);
    }

    @Test
    void savingLocationWithoutId_shouldReturnForecastWithId() {
        //given
        Long forecastIdBeforeSaving = forecast.getId();
        //when
        Long forecastIdAfterSaving = mockForecastRepository.save(forecast, location).getId();
        //then
        assertThat(forecastIdBeforeSaving).isNotEqualTo(forecastIdAfterSaving);
    }

    @Test
    void getLastForecastForLocation_shouldReturnForecast_whenPassedTwoDays() {
        //given
        Instant requestedForecastDate = clock.instant().plus(2, ChronoUnit.DAYS);
        Instant requestDate = clock.instant();
        //when
        Optional<Forecast> forecastOptional = mockForecastRepository.getActiveForecastForRequiredLocationAndDay(1L, requestedForecastDate, requestDate);
        //then
        assertThat(forecastOptional).isNotEmpty();
    }
    @Test
    void getLastForecastForLocation_shouldReturnForecastWithId1_whenPassedTwoDays() {
        //given
        Instant requestedForecastDate = clock.instant().plus(2, ChronoUnit.DAYS);
        Instant requestDate = clock.instant();
        //when
        Optional<Forecast> forecastOptional = mockForecastRepository.getActiveForecastForRequiredLocationAndDay(1L, requestedForecastDate, requestDate);
        //then
        assertThat(forecastOptional.get().getId()).isEqualTo(1);
    }
    @Test
    void getLastForecastForLocation_shouldReturnForecastWithId3_whenPassedSixDays() {
        //given
        Instant requestedForecastDate = clock.instant().plus(6, ChronoUnit.DAYS);
        Instant requestDate = clock.instant();
        //when
        Optional<Forecast> forecastOptional = mockForecastRepository.getActiveForecastForRequiredLocationAndDay(1L, requestedForecastDate, requestDate);
        //then
        assertThat(forecastOptional.get().getId()).isEqualTo(3);
    }
    @Test
    void getLastForecastForLocation_shouldReturnEmptyOptional_whenPassed3Days() {
        //given
        Instant requestedForecastDate = clock.instant().plus(3, ChronoUnit.DAYS);
        Instant requestDate = clock.instant();
        //when
        Optional<Forecast> forecastOptional = mockForecastRepository.getActiveForecastForRequiredLocationAndDay(1L, requestedForecastDate, requestDate);
        //then
        assertThat(forecastOptional).isEmpty();
    }

    @Test
    void getLastForecastForLocation_shouldReturnForecast_whenRequestDateIs23h59m59sDifferent() {
        //given
        Instant requestedForecastDate = clock.instant().plus(2, ChronoUnit.DAYS);
        Instant requestDate = clock.instant()
                .plus(23, ChronoUnit.HOURS)
                .plus(59, ChronoUnit.MINUTES)
                .plus(59, ChronoUnit.SECONDS);
        //when
        Optional<Forecast> forecastOptional = mockForecastRepository.getActiveForecastForRequiredLocationAndDay(1L, requestedForecastDate, requestDate);
        //then
        assertThat(forecastOptional).isNotEmpty();
    }
    @Test
    void getLastForecastForLocation_shouldReturnEmptyOptional_whenRequestDateIs24h00m01sDifferent() {
        //given
        Instant requestedForecastDate = clock.instant().plus(2, ChronoUnit.DAYS);
        Instant requestDate = clock.instant()
                .plus(24, ChronoUnit.HOURS)

                .plus(1, ChronoUnit.SECONDS);
        //when
        Optional<Forecast> forecastOptional = mockForecastRepository.getActiveForecastForRequiredLocationAndDay(1L, requestedForecastDate, requestDate);
        //then
        assertThat(forecastOptional).isEmpty();
    }
}