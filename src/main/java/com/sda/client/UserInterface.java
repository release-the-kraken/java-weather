package com.sda.client;

import com.sda.location.LocationController;
import lombok.RequiredArgsConstructor;

import java.util.Scanner;

@RequiredArgsConstructor
public class UserInterface {
    private final LocationController locationController;

    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Application running\n");
        System.out.println("Welcome to Weather App.\nWhat would you like to do?");

        while (true) {
            System.out.println(" 1. Add a location");
            System.out.println(" 2. Get location by id");
            System.out.println(" 0. Close app");
            int option = scanner.nextInt();

            switch (option) {
                case 1:
                    createLocation(scanner);
                    break;
                case 2:
                    getLocationById(scanner);
                case 0:
                    return;
            }
        }
    }

    private void createLocation(Scanner scanner) {
        scanner.nextLine();
        System.out.println("Insert city - required");
        String city = scanner.nextLine();
        System.out.println("Insert region");
        String region = scanner.nextLine();
        System.out.println("Insert country - required");
        String country = scanner.nextLine();
        System.out.println("Insert longitude in 00,00 format- required");
        Double longitude = scanner.nextDouble();
        System.out.println("Insert latitude 00,00 format - required");
        Double latitude = scanner.nextDouble();
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
    private void getLocationById(Scanner scanner){
        scanner.nextLine();
        System.out.println("Insert location id");
        Long locationId = scanner.nextLong();
        scanner.nextLine();
        String response = locationController.getLocationById(locationId);
        System.out.println("Server response: %s".formatted(response));
    }
}
