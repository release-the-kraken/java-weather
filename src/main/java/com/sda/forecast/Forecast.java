package com.sda.forecast;

import com.sda.location.Location;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Forecast {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    float temperature;
    int humidity;
    float windSpeed;
    int pressure;
    String windDirection;
    @ManyToOne
    Location location;
    Instant createdDate;
    Instant forecastDate;
}
