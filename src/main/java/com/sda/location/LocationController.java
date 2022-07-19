package com.sda.location;

import com.google.gson.Gson;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;


@RequiredArgsConstructor
public class LocationController {
    private final LocationService locationService;
    private final Gson gson;

    //POST:/locations
    public String createLocation(String json) {
        try {
            LocationDTO locationDTO = gson.fromJson(json, LocationDTO.class);
            String city = locationDTO.getCity();
            String region = locationDTO.getRegion();
            String country = locationDTO.getCountry();
            Double longitude = locationDTO.getLongitude();
            Double latitude = locationDTO.getLatitude();
            Location location = locationService.create(city, region, country, longitude, latitude);
            LocationDTO response = mapToLocationDTO(location);
            return gson.toJson(response);
        } catch (IllegalArgumentException e) {
            return "Error message: %s".formatted(e.getMessage());
        }
    }

    private LocationDTO mapToLocationDTO(Location location) {
        return LocationDTO.builder()
                .id(location.getId())
                .city(location.getCity())
                .region(location.getRegion())
                .country(location.getCountry())
                .longitude(location.getLongitude())
                .latitude(location.getLatitude())
                .build();
    }

    // GET:/locations
    String getLocations() {
        return "";
    }
}