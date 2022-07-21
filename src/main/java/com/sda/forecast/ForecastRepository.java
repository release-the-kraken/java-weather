package com.sda.forecast;

import com.sda.location.Location;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

@RequiredArgsConstructor
public class ForecastRepository {
    private final SessionFactory sessionFactory;

    Forecast save(Forecast forecast, Location location) {
        Session session = sessionFactory.openSession();
        try (session) {
            Transaction transaction = session.beginTransaction();
            //forecast.setCreatedDate(LocalDateTime.now().toInstant(ZoneOffset.ofHours(2)));
            location.addForecast(forecast);
            session.persist(location);
            transaction.commit();
            return forecast;
        } catch (Exception e) {
            session.getTransaction()
                    .rollback();
            System.out.println("Database operation failed. Error message: %s".formatted(e.getMessage()));
            return null;
        }
    }

    Optional<Forecast> getLastForecastForLocation(String city) {
        Session session = sessionFactory.openSession();
        try (session) {
            Transaction transaction = session.beginTransaction();
            Forecast forecast = session
                    .createQuery("SELECT f FROM Forecast f " +
                                    "LEFT JOIN FETCH l.forecasts " +
                                    "WHERE city=:city " +
                                    "ORDER BY f.createdDate DESC LIMIT 1",
                            Forecast.class)
                    .setParameter("city", city)
                    .getSingleResult();
            transaction.commit();
            return Optional.ofNullable(forecast);
        } catch (Exception e) {
            session.getTransaction()
                    .rollback();
            System.out.println("Database operation failed. Error message: %s".formatted(e.getMessage()));
            return Optional.empty();
        }
    }
}
