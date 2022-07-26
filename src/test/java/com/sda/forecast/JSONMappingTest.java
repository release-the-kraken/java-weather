package com.sda.forecast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sda.location.LocationRepository;
import com.sda.location.LocationService;
import com.sda.location.MockLocationRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.Clock;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ObjectCreationFromHttpRequestTest {
    ForecastRepository forecastRepository;
    LocationService locationService;
    LocationRepository locationRepository;
    HttpRequestClient mockHttpClient = new MockHttpForecastRequestClient();
    ForecastService forecastService;
    String json = mockHttpClient.getWeatherData(53.3, 15.03);
    ObjectMapper objectMapper ;
    Clock clock;
    @BeforeAll
    void setUp() {
        objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        locationRepository = new MockLocationRepository();
        locationService = new LocationService(locationRepository);
        forecastRepository = new MockForecastRepositoryImpl(clock);
        forecastService = new ForecastService(forecastRepository, locationService, locationRepository, mockHttpClient, objectMapper);
    }

    @Test
    void MappingJsonToObject_ShouldCreateForecastObject() throws JsonProcessingException {
        //given
        ForecastClientResponseDTO responseDTO = null;
        //when
        responseDTO = objectMapper.readValue(json, ForecastClientResponseDTO.class);
        //then
        assertThat(responseDTO).isNotNull();
    }
    @Test
    void mapToSingleForecast_shouldCreateNotNullSingleForecastObject() throws JsonProcessingException {
        //given
        ForecastClientResponseDTO responseDTO = objectMapper.readValue(json, ForecastClientResponseDTO.class);
        SingleForecast singleForecast = null;
        //when
        singleForecast = forecastService.mapToSingleForecast(responseDTO, 1);
        //then
        assertThat(singleForecast).isNotNull();
    }
    @Test
    void mapToForecastEntity() throws JsonProcessingException {
        //given
        ForecastClientResponseDTO responseDTO = objectMapper.readValue(json, ForecastClientResponseDTO.class);
        SingleForecast singleForecast = forecastService.mapToSingleForecast(responseDTO, 1);
        Forecast forecast = null;
        //when
        forecast = forecastService.mapToForecastEntity(singleForecast);
        //then
        assertThat(forecast).isNotNull();
    }
}