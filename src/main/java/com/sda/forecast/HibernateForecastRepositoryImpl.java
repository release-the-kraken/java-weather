package com.sda.forecast;

import com.sda.location.Location;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.time.*;
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

    @Override
    public Optional<Forecast> getLastForecastForLocation(Long id, int days) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            Forecast forecast = session
                    .createQuery("SELECT f FROM Forecast f " +
                                    "WHERE f.location.id = :id",
                            Forecast.class)
                    .setParameter("id", id)
                    .stream()
                    .filter(f -> filterActiveForecasts(days, f))
                    .findFirst()
                    .orElse(null);

            transaction.commit();
            session.close();
            return Optional.ofNullable(forecast);
        } catch (Exception e) {
            System.out.println("Database operation failed. Error message: %s".formatted(e.getMessage()));
            return Optional.empty();
        }
    }

    private boolean filterActiveForecasts(int days, Forecast f) {
        ZoneId zoneId = ZoneId.of("Europe/Warsaw");
        ZoneOffset zoneOffset = zoneId.getRules().getOffset(LocalDateTime.now());
        LocalDateTime forecastCreatedDate = LocalDateTime.ofInstant(f.getCreatedDate(), zoneOffset);
        LocalDateTime userRequiredDate = LocalDateTime.now().plusDays(days);
        Duration duration = Duration.between(userRequiredDate, forecastCreatedDate);
        long diff = duration.toHours();
        if ((diff < 24 && !duration.isNegative())) {
            return true;
        } else {
            return false;
        }
    }
}
