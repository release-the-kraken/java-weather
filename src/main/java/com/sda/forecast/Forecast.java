package com.sda.forecast;

import com.google.gson.annotations.Expose;
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
    @ManyToOne
    Location location;
    int windDirection;
    Instant createdDate;//data utworzenia encji/rekordu, pobrania prognozy
    Instant forecastDate;//data dla której chcemy prognozę
}
