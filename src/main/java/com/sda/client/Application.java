package com.sda.client;

import com.google.gson.Gson;
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
        Gson gson = new Gson();
        LocationRepository locationRepository = new LocationRepository(sessionFactory);
        LocationService locationService = new LocationService(locationRepository);
        LocationController locationController = new LocationController(locationService, gson);

        ForecastRepository forecastRepository = new ForecastRepository(sessionFactory);
        ForecastService forecastService = new ForecastService(forecastRepository, locationController, gson);
        ForecastController forecastController = new ForecastController(forecastService, gson);

        UserInterface userInterface = new UserInterface(locationController, forecastController, gson);

        userInterface.run();

    }
}
