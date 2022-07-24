package com.sda.location;

import jdk.jfr.Name;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

//@NoArgsConstructor
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocationDTO {
    private Long id;
    private String city;
    private String region;
    private String country;
    private Double longitude;
    private Double latitude;

}
