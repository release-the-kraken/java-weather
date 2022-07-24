package com.sda.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sda.forecast.ForecastController;
import com.sda.location.LocationController;
import com.sda.location.LocationDTO;
import lombok.RequiredArgsConstructor;

import java.util.*;

@RequiredArgsConstructor
public class UserInterface {
    private final LocationController locationController;
    private final ForecastController forecastController;
    private final ObjectMapper objectMapper;

    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Application running\n");
        System.out.println("Welcome to Weather App.\nWhat would you like to do?");

        while (true) {
            System.out.println("Available locations:");
            try {
                displayLocations();
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            System.out.println("\t1. Add a location");
            System.out.println("\t2. Get full location data by id");
            System.out.println("\t3. Get full data for all locations");
            System.out.println("\t4. Get forecast for location");
            System.out.println("\t0. Close app");
            int option = scanner.nextInt();

            switch (option) {
                case 1:
                    createLocation(scanner);
                    break;
                case 2:
                    getLocationById(scanner);
                    break;
                case 3:
                    getAllLocations(scanner);
                    break;
                case 4:
                    createForecast(scanner);
                    break;
                case 0:
                    return;
            }
        }
    }

    private void createForecast(Scanner scanner) {
        scanner.nextLine();
        System.out.println("Insert city id");
        Long cityId = scanner.nextLong();
        System.out.println("Insert number of days ahead");
        int daysAhead = scanner.nextInt();
        String response;
        try {
            response = forecastController.getForecast(cityId, daysAhead);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Server response: %s".formatted(response));
    }

    private void createLocation(Scanner scanner) {
        scanner.nextLine();
        System.out.println("Insert city - required");
        String city = scanner.nextLine();
        System.out.println("Insert region");
        String region = scanner.nextLine();
        System.out.println("Insert country - required");
        String country = scanner.nextLine();
        System.out.println("Insert latitude 00,00 format - required");
        Double latitude = scanner.nextDouble();
        System.out.println("Insert longitude in 00,00 format- required");
        Double longitude = scanner.nextDouble();
        scanner.nextLine();
        String request = createMockRequest(city, region, country, longitude, latitude);
        System.out.println("Sending Http request: %s".formatted(request));
        String response = locationController.createLocation(request);
        System.out.println("Server response: %s".formatted(response));
    }

    private String createMockRequest(String city, String region, String country, Double longitude, Double latitude) {
        return "{\"city\":\"%s\", \"region\":\"%s\", \"country\":\"%s\", \"longitude\":\"%s\", \"latitude\":\"%s\"}"
                .formatted(city, region, country, longitude, latitude);
    }

    private void getAllLocations(Scanner scanner) {
        String response = locationController.getLocations();
        System.out.println("All saved locations");
        System.out.println("Server response: %s".formatted(response));
    }
    private void getLocationById(Scanner scanner){
        scanner.nextLine();
        System.out.println("Insert location id");
        Long locationId = scanner.nextLong();
        scanner.nextLine();
        String response = locationController.getLocationById(locationId);
        System.out.println("Server response: %s".formatted(response));
    }
    private void displayLocations() throws JsonProcessingException {
        LocationDTO[] locations = objectMapper.readValue(locationController.getLocations(), LocationDTO[].class);
        Arrays.stream(locations)
                .map(location -> "Id: " + location.getId() +", " + location.getCity() + ", " + location.getCountry())
                .forEach(System.out::println);
    }
}
