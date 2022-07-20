package com.sda.forecast;

import com.sda.location.Location;
import lombok.*;

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
    int windDirection;
    @ManyToOne
    Location location;
    Instant createdDate;
    Instant forecastDate;
}
