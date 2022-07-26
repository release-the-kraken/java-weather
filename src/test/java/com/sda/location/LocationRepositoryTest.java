package com.sda.location;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class LocationRepositoryTest {
    LocationRepository locationRepository = new MockLocationRepository();

    @Test
    void save_shouldReturnPassedObjectWithId() {
        //given
        Location location = new Location();
        Long locationIdBeforeSaving = location.getId();
        //when
        location = locationRepository.save(location);
        Long locationIdAfterSaving = location.getId();
        //then
        assertThat(locationIdBeforeSaving).isNotEqualTo(locationIdAfterSaving);
    }

    @Test
    void findAll_shouldReturnNonEmptyList() {
        //when
        List<Location> locations = locationRepository.findAll();
        //then
        assertThat(locations).isNotEmpty();
    }

    @Test
    void findById_shouldReturnNonEmptyOptionalForId1() {
        //when
        Optional<Location> locationOptional = locationRepository.findById(1L);
        //then
        assertThat(locationOptional).isNotEmpty();
    }
}