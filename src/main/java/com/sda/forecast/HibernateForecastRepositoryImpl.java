package com.sda.forecast;

import com.sda.location.Location;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.util.Optional;

@RequiredArgsConstructor
public class HibernateForecastRepositoryImpl implements ForecastRepository {
    private final SessionFactory sessionFactory;

    @Override
    public Forecast save(Forecast forecast, Optional<Location> location) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Location loc = location.get();//todo handle optional better
        try {
            loc.addForecast(forecast);
            session.persist(forecast);
            transaction.commit();
            return forecast;
        } catch (Exception e) {
            System.out.println("Database operation failed. Error message: %s".formatted(e.getMessage()));
            return null;
        }
    }
    @Override
    public Optional<Forecast> getLastForecastForLocation(Long id) {
        LocalDate requiredForecastDate = LocalDate.now(); // todo missing forecastDate parameter, check where it appears and add number of days passed by user
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            Forecast forecast = session
                    .createQuery("SELECT f FROM Forecast f " +
                                    "WHERE f.location.id = :id " + //todo add forecast date to query
                                    "ORDER BY f.forecastDate",
                            Forecast.class)
                    .setParameter("id", id)
                    .setMaxResults(1)
                    .getSingleResult();

            transaction.commit();
            session.close();
            return Optional.ofNullable(forecast);
        } catch (Exception e) {
            System.out.println("Database operation failed. Error message: %s".formatted(e.getMessage()));

            return Optional.empty();
        }
    }
}
