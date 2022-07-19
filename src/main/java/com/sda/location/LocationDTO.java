package com.sda.location;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class LocationDTO {
    private Long id;
    private final String city;
    private String region;
    private final String country;
    private final Double longitude;
    private final Double latitude;

}
