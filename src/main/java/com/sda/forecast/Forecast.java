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
    int pressure;
    float windSpeed;
    int windDirection;
    Instant createdDate;
    Instant forecastDate;
    @ManyToOne
    Location location;
}
