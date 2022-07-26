package com.sda.location;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class LocationService {
    private final LocationRepository locationRepository;
    private static final Double MIN_LATITUDE = -90.00;
    private static final Double MAX_LATITUDE = 90.00;
    private static final Double MIN_LONGITUDE = -180.00;
    private static final Double MAX_LONGITUDE = 180.00;
    Location create(String city, String region, String country, Double longitude, Double latitude) {
        if(city == null || city.isBlank()){
            throw new IllegalArgumentException("City can't be empty");
        }
        if(country == null || country.isBlank()){
            throw new IllegalArgumentException("Country can't be empty");
        }
        if(longitude == null || longitude < MIN_LONGITUDE || longitude > MAX_LONGITUDE){
            throw new IllegalArgumentException("Longitude can't be empty and must be between -180.00 and 180.00");
        }
        if(latitude == null || latitude < MIN_LATITUDE || latitude > MAX_LATITUDE){
            throw new IllegalArgumentException("Latitude can't be empty and must be between -90.00 and 90.00");
        }
        if(region.isBlank()){
            region = "N/A";
        }
        Location location = new Location();
        location.setCity(city);
        location.setRegion(region);
        location.setCountry(country);
        location.setLongitude(longitude);
        location.setLatitude(latitude);
        ZoneId zoneId = ZoneId.of("Europe/Warsaw");
        ZoneOffset zoneOffset = zoneId.getRules().getOffset(LocalDateTime.now());
        location.setCreatedDate(LocalDateTime.now().toInstant(zoneOffset));
        return locationRepository.save(location);
    }
    public LocationDTO getByID(Long id){
        Optional<Location> locationOptional = locationRepository.findById(id);
        Location location = locationOptional
                .orElseThrow(() -> new IllegalArgumentException("No entry with id %s.".formatted(id)));
        return LocationDTO.builder()
                .id(location.getId())
                .city(location.getCity())
                .region(location.getRegion())
                .longitude(location.getLongitude())
                .latitude(location.getLatitude())
                .build();
    }
    List<Location> getAll(){
        return locationRepository.findAll();
    }
}
