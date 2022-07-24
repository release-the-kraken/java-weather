package com.sda.location;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class LocationController {
    private final LocationService locationService;
    private final ObjectMapper objectMapper;

    //POST:/locations
    public String createLocation(String json) {
        try {
            LocationDTO locationDTO = objectMapper.readValue(json, LocationDTO.class);
            String city = locationDTO.getCity();
            String region = locationDTO.getRegion();
            String country = locationDTO.getCountry();
            Double longitude = locationDTO.getLongitude();
            Double latitude = locationDTO.getLatitude();
            Location location = locationService.create(city, region, country, longitude, latitude);
            LocationDTO response = mapToLocationDTO(location);
            return objectMapper.writeValueAsString(response);
        } catch (IllegalArgumentException e) {
            return "Error message: %s".formatted(e.getMessage());
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    //GET:/locations

    public String getLocationById(Long id) {
        try {
            LocationDTO locationDTO = locationService.getByID(id);
            return objectMapper.writeValueAsString(locationDTO);
        } catch (IllegalArgumentException e) {
            return "Error message: %s".formatted(e.getMessage());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
    // GET:/locations

    public String getLocations() {
        List<Location> locations = locationService.getAll();
        List<LocationDTO> locationDTOs = locations
                .stream()
                .map(location -> mapToLocationDTO(location))
                .toList();
        return locationDTOs
                .stream()
                .map(ld -> {
                    try {
                        return objectMapper.writeValueAsString(ld);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.joining(",", "[", "]"));
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
}
