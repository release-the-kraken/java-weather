package com.sda.location;

import com.google.gson.annotations.Expose;
import com.sda.forecast.Forecast;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String city;
    String region;
    String country;
    Double longitude;
    Double latitude;
    Instant createdDate;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "location")
    Set<Forecast> forecasts = new HashSet<>();

    public void addForecast(Forecast forecast) {
        forecasts.add(forecast);
        forecast.setLocation(this);
    }
}
