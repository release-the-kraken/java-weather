package com.sda.forecast;

import com.sda.location.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ForecastRepositoryTest {
    Forecast forecast;
    Location location;
    MockForecastRepositoryImpl mockForecastRepository;
    HibernateForecastRepositoryImpl hibernateForecastRepository;
    @BeforeEach
    void setUp(){
        forecast = new Forecast();
        location = new Location();
        mockForecastRepository = new MockForecastRepositoryImpl();
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
    void getLastForecastForLocation_OneDay() {
        //given

        //when
        Optional<Forecast> forecastOptional = mockForecastRepository.getActiveForecastForRequiredLocationAndDay(1L, 1);
        //then
        assertThat(forecastOptional).isNotEmpty();
    }
}