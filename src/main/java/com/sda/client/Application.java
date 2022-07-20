package com.sda.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sda.forecast.ForecastController;
import com.sda.forecast.ForecastRepository;
import com.sda.forecast.ForecastService;
import com.sda.location.LocationController;
import com.sda.location.LocationRepository;
import com.sda.location.LocationService;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class Application {
    public static void main(String[] args) {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();
        SessionFactory sessionFactory = new MetadataSources(registry)
                .buildMetadata()
                .buildSessionFactory();
        ObjectMapper objectMapper = new ObjectMapper();

        LocationRepository locationRepository = new LocationRepository(sessionFactory);
        LocationService locationService = new LocationService(locationRepository);
        LocationController locationController = new LocationController(locationService, objectMapper);

        ForecastRepository forecastRepository = new ForecastRepository(sessionFactory);
        ForecastService forecastService = new ForecastService(forecastRepository, locationController, objectMapper);
        ForecastController forecastController = new ForecastController(forecastService, objectMapper);

        UserInterface userInterface = new UserInterface(locationController, forecastController, objectMapper);

        userInterface.run();

    }
}
