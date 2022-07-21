package com.sda.forecast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JSONMappingTest {
    String json = "{\"lat\": 49, \"lon\": 22, \"daily\":[" +
            "{\"dt\":1658397600, \"moonrise\": 0, \"temp\":{" +
            "\"day\": 29.22," +
            " \"night\": 19.06}," +
            " \"pressure\": 1018, \"humidity\": 21, \"wind_speed\": 4.17, \"wind_deg\": 29,\"wind_gust\": 6.32}]}";
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    void setUp() {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Test
    void MappingJsonToObject_ShouldCreateForecastObject() throws JsonProcessingException {
        ForecastClientResponseDTO responseDTO = null;
        responseDTO = objectMapper.readValue(json, ForecastClientResponseDTO.class);

    }
}