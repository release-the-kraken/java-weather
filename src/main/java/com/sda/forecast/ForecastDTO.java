package com.sda.forecast;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ForecastDTO {
    Long id;
    float temperature; //C
    int humidity; //%
    float windSpeed; //m/s
    int pressure; //hPa
    String windDirection; //N, S, W, E, NW...
}
