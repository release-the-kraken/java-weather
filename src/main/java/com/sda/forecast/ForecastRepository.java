package com.sda.forecast;

import com.sda.location.Location;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

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
}
