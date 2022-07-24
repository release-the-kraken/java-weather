package com.sda.location;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class HibernateLocationRepositoryImpl implements LocationRepository{
    private final SessionFactory sessionFactory;
    @Override
    public Location save(Location location) {
        Session session = sessionFactory.openSession();
        try (session) {
            Transaction transaction = session.beginTransaction();
            session.persist(location);
            transaction.commit();
            return location;
        } catch (Exception e) {
            session.getTransaction()
                    .rollback();
            System.out.println("Database operation failed. Error message: %s".formatted(e.getMessage()));
            return null;
        }
    }
    @Override
    public List<Location> findAll() {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        try {
            List<Location> locations = session.createQuery("select l from Location l", Location.class).getResultList();
            transaction.commit();
            return locations;
        } catch (Exception e) {
            transaction.rollback();
            System.out.println("Database operation failed. Error message: %s".formatted(e.getMessage()));
            return Collections.emptyList();
        }
    }
    @Override
    public Optional<Location> findById(Long id) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        try {
            Location location = session
                    .createQuery("select l from Location l where id=:id", Location.class)
                    .setParameter("id", id)
                    .getSingleResult();
            transaction.commit();
            session.close();
            return Optional.ofNullable(location);
        } catch (Exception e) {
            transaction.rollback();
            System.out.println("Database operation failed. Error message: %s".formatted(e.getMessage()));
            return Optional.empty();
        }
    }
}
