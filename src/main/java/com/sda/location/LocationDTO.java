package com.sda.location;

import lombok.Data;

import java.time.Instant;

@Data
public class LocationDTO {
    Long id;
    String city;
    String region;
    String country;
    Double longitude;
    Double latitude;

}
