package com.sda.location;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
@RequiredArgsConstructor
public class LocationRepository {
    private final SessionFactory sessionFactory;
    Location save (Location location){
        Session session = sessionFactory.openSession();
        try(session){
            Transaction transaction = session.beginTransaction();
            session.persist(location);
            transaction.commit();
            return location;
        }catch(Exception e){
            session
                    .getTransaction()
                    .rollback();
            System.out.println("Database operation failed. Error message: %s".formatted(e.getMessage()));
            return null;
        }
    }

    List<Location> findAll(){
        return Collections.emptyList();
    }

    Optional<Location> findById(Long id){
        Session session = sessionFactory.openSession();
        try(session){
            Transaction transaction = session.beginTransaction();
            Location location = session.createQuery("select l from location l where id=:id", Location.class).getSingleResult();
            transaction.commit();
            return Optional.ofNullable(location);
        }catch(Exception e){
            session
                    .getTransaction()
                    .rollback();
            System.out.println("Database operation failed. Error message: %s".formatted(e.getMessage()));
            return Optional.empty();
        }
    }
}
