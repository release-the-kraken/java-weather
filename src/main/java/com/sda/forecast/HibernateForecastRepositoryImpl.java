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
    public Forecast save(Forecast forecast, Location location) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            location.addForecast(forecast);
            session.persist(forecast);
            transaction.commit();
            return forecast;
        } catch (Exception e) {
            System.out.println("Database operation failed. Error message: %s".formatted(e.getMessage()));
            return null;
        }
    }

    Optional<Forecast> getLastForecastForLocation(Long id) {
        LocalDate forecastDate = LocalDate.now(); // todo missing forecastDate parameter
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {

            // todo you can use this:
            //  session.createQuery("SELECT f FROM Forecast f " +
            //      "WHERE f.location.id = id " +
            //      "AND f.forecastDate = :forecastDate " +
            //      "ORDER BY f.forecastDate");

            Forecast forecast = session
                    .createQuery("SELECT f FROM Forecast f WHERE f.location.id = :id ORDER BY f.forecastDate",
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
