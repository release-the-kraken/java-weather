package com.sda.forecast;

import com.sda.location.Location;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

@RequiredArgsConstructor
public class ForecastRepository {
    private final SessionFactory sessionFactory;
    Forecast save(Forecast forecast) {
        Session session = sessionFactory.openSession();
        try (session) {
            Transaction transaction = session.beginTransaction();
            session.persist(forecast);
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
