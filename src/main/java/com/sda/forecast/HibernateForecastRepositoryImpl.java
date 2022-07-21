package com.sda.forecast;

import com.sda.location.Location;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.Optional;

@RequiredArgsConstructor
public class HibernateForecastRepositoryImpl implements ForecastRepository{
    private final SessionFactory sessionFactory;
    @Override
    public Forecast save(Forecast forecast, Location location) {
        Session session = sessionFactory.openSession();
        try (session) {
            Transaction transaction = session.beginTransaction();
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

    Optional<Forecast> getLastForecastForLocation(Long id) {
        Session session = sessionFactory.openSession();
        try (session) {
            Transaction transaction = session.beginTransaction();
            Forecast forecast = session
                    .createQuery("SELECT l FROM Location l " +
                                    "JOIN FETCH l.forecasts f" +
                                    "WHERE id = :id" +
                                    "ORDER BY f.id",
                            Forecast.class)
                    .setParameter("id", id)
                    .setMaxResults(1)
                    .getSingleResult();
/*            Forecast forecast = session.find(Location.class, city)
                    .getForecasts()
                    .stream()
                    .findFirst()
                    .orElse(null);*/
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
