package com.sda.forecast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ObjectCreationFromHttpRequestTest {
    HttpRequestClient mockHttpClient = new MockHttpForecastRequestClient();
    String json = mockHttpClient.getWeatherData(53.3, 15.03);
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    void setUp() {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
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
}