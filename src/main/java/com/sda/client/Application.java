package com.sda.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sda.forecast.ForecastController;
import com.sda.forecast.ForecastHttpRequestClient;
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
objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        LocationRepository locationRepository = new LocationRepository(sessionFactory);
        LocationService locationService = new LocationService(locationRepository);
        LocationController locationController = new LocationController(locationService, objectMapper);

        ForecastHttpRequestClient forecastHttpRequestClient = new ForecastHttpRequestClient();
        ForecastRepository forecastRepository = new ForecastRepository(sessionFactory);
        ForecastService forecastService = new ForecastService(forecastRepository, locationController, forecastHttpRequestClient, objectMapper);
        ForecastController forecastController = new ForecastController(forecastService, objectMapper);

        UserInterface userInterface = new UserInterface(locationController, forecastController, objectMapper);

        userInterface.run();

    }
}
