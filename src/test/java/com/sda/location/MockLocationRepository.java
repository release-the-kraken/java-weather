package com.sda.location;

import com.sda.location.Location;
import com.sda.location.LocationRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;



public class MockLocationRepository implements LocationRepository {
    @Override
    public Location save(Location location) {
        location.setId(1L);
        return location;
    }

    @Override
    public List<Location> findAll() {
        Location location1 = new Location();
        location1.setId(1L);
        location1.setCity("City1");
        Location location2 = new Location();
        location2.setId(2L);
        location2.setCity("City1");
        Location location3 = new Location();
        location3.setId(3L);
        location3.setCity("City1");

        return List.of(location1, location2, location3);
    }

    @Override
    public Optional<Location> findById(Long id) {
        Location location1 = new Location();
        location1.setId(1L);
        location1.setCity("City1");
        Location location2 = new Location();
        location2.setId(2L);
        location2.setCity("City1");
        Location location3 = new Location();
        location3.setId(3L);
        location3.setCity("City1");
        Map<Long, Location> mockDatabase = new HashMap<>();
        mockDatabase.put(location1.getId(), location1);
        mockDatabase.put(location2.getId(), location2);
        mockDatabase.put(location3.getId(), location3);
        return Optional.ofNullable(mockDatabase.get(id));
    }
}
