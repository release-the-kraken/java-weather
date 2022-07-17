package com.sda.forecast;

import lombok.Data;

@Data
public class ForecastDTO {
    Long id;
    float temperature; //C
    int humidity; //%
    float windSpeed; //m/s
    int pressure; //hPa
    String windDirection; //N, S, W, E, NW...
}
