package dat.config;


import dat.daos.impl.GuideDAO;
import dat.daos.impl.UserDAO;
import dat.entities.Guide;
import dat.entities.Trip;
import dk.bugelhartmann.UserDTO;
import dat.security.entities.User;
import dat.security.entities.Role;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Populate class
 * Purpose: To populate data for our tests
 * When main is run in Main, this class's main method is also run, which populates data we can use for our .http file
 */

public class Populate {
    private static EntityManagerFactory emf;

    public Populate(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public static UserDTO[] populateUsers(EntityManagerFactory emf) {
        User user, admin;
        Role userRole, adminRole;

        user = new User("usertest", "user123");
        admin = new User("admintest", "admin123");

        try (var em = emf.createEntityManager())
        {
            em.getTransaction().begin();

            userRole = em.createQuery("SELECT r FROM Role r WHERE r.name = :name", Role.class)
                    .setParameter("name", "USER")
                    .getResultStream()
                    .findFirst()
                    .orElseGet(() -> {
                        Role newRole = new Role("USER");
                        em.persist(newRole);
                        return newRole;
                    });

            adminRole = em.createQuery("SELECT r FROM Role r WHERE r.name = :name", Role.class)
                    .setParameter("name", "ADMIN")
                    .getResultStream()
                    .findFirst()
                    .orElseGet(() -> {
                        Role newRole = new Role("ADMIN");
                        em.persist(newRole);
                        return newRole;
                    });

            user.addRole(userRole);
            admin.addRole(adminRole);

            em.persist(userRole);
            em.persist(adminRole);
            em.persist(user);
            em.persist(admin);
            em.getTransaction().commit();
        }
        UserDTO userDTO = new UserDTO(user.getUsername(), "user123");
        UserDTO adminDTO = new UserDTO(admin.getUsername(), "admin123");
        return new UserDTO[]{userDTO, adminDTO};
    }

    public void cleanUpData(){
        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();
            // Delete rooms first to avoid foreign key constraint violations
            em.createQuery("DELETE FROM User").executeUpdate();
            em.createQuery("DELETE FROM Trip").executeUpdate();
            em.createQuery("DELETE FROM Guide").executeUpdate();
            em.createNativeQuery("ALTER SEQUENCE trip_id_seq RESTART WITH 1").executeUpdate();
            em.createNativeQuery("ALTER SEQUENCE guide_guide_id_seq RESTART WITH 1").executeUpdate();
            em.getTransaction().commit();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void populateDataWithRequest() {

        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();
// Guide dummy data
            Guide guide1 = new Guide("John", "Smith", 21, "john.smith@school.com", 1234567890);
            Guide guide2 = new Guide("Emily", "Taylor", 14, "emily.taylor@school.com", 987654321);
            Guide guide3 = new Guide("Michael", "Lee", 23, "michael.lee@school.com", 1122334455);
            Guide guide4 = new Guide("Lisa", "Chen", 9, "lisa.chen@school.com", 988776655);
            Guide guide5 = new Guide("Juan", "Martinez", 30, "juan.martinez@school.com", 677889900);

// Trip dummy data with complete fields
            Trip trip1 = new Trip("USA Trip", LocalDate.of(2024, 11, 5), LocalTime.of(10, 0), LocalTime.of(11, 0), -122.4194, 37.7749, 150.0, Trip.Category.city);
            Trip trip2 = new Trip("BonBon Land", LocalDate.of(2024, 11, 6), LocalTime.of(14, 0), LocalTime.of(15, 0), -73.935242, 40.73061, 200.0, Trip.Category.beach);
            Trip trip3 = new Trip("Tivoli", LocalDate.of(2024, 11, 7), LocalTime.of(9, 30), LocalTime.of(10, 30), -87.623177, 41.881832, 175.0, Trip.Category.snow);
            Trip trip4 = new Trip("Bornholm", LocalDate.of(2024, 11, 7), LocalTime.of(13, 0), LocalTime.of(14, 0), -95.358421, 29.749907, 180.0, Trip.Category.lake);
            Trip trip5 = new Trip("Germany", LocalDate.of(2024, 11, 8), LocalTime.of(9, 30), LocalTime.of(10, 30), -118.243683, 34.052235, 160.0, Trip.Category.city);

            trip1.setGuide(guide1);
            trip2.setGuide(guide2);
            trip3.setGuide(guide3);
            trip4.setGuide(guide4);
            trip5.setGuide(guide4);

            em.persist(guide4);
            em.persist(guide1);
            em.persist(guide2);
            em.persist(guide3);
            em.persist(guide5);

            em.persist(trip1);
            em.persist(trip2);
            em.persist(trip3);
            em.persist(trip4);
            em.persist(trip5);

            em.getTransaction().commit();
        }
    }


    public static void main(String[] args) {

        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();
// Guide dummy data
            Guide guide1 = new Guide("John", "Smith", 21, "john.smith@school.com", 1234567890);
            Guide guide2 = new Guide("Emily", "Taylor", 14, "emily.taylor@school.com", 987654321);
            Guide guide3 = new Guide("Michael", "Lee", 23, "michael.lee@school.com", 1122334455);
            Guide guide4 = new Guide("Lisa", "Chen", 9, "lisa.chen@school.com", 988776655);
            Guide guide5 = new Guide("Juan", "Martinez", 30, "juan.martinez@school.com", 677889900);

// Trip dummy data with complete fields
            Trip trip1 = new Trip("USA Trip", LocalDate.of(2024, 11, 5), LocalTime.of(10, 0), LocalTime.of(11, 0), -122.4194, 37.7749, 150.0, Trip.Category.city);
            Trip trip2 = new Trip("BonBon Land", LocalDate.of(2024, 11, 6), LocalTime.of(14, 0), LocalTime.of(15, 0), -73.935242, 40.73061, 200.0, Trip.Category.beach);
            Trip trip3 = new Trip("Tivoli", LocalDate.of(2024, 11, 7), LocalTime.of(9, 30), LocalTime.of(10, 30), -87.623177, 41.881832, 175.0, Trip.Category.snow);
            Trip trip4 = new Trip("Bornholm", LocalDate.of(2024, 11, 7), LocalTime.of(13, 0), LocalTime.of(14, 0), -95.358421, 29.749907, 180.0, Trip.Category.lake);
            Trip trip5 = new Trip("Germany", LocalDate.of(2024, 11, 8), LocalTime.of(9, 30), LocalTime.of(10, 30), -118.243683, 34.052235, 160.0, Trip.Category.city);

            trip1.setGuide(guide1);
            trip2.setGuide(guide2);
            trip3.setGuide(guide3);
            trip4.setGuide(guide4);
            trip5.setGuide(guide4);

            em.persist(guide4);
            em.persist(guide1);
            em.persist(guide2);
            em.persist(guide3);
            em.persist(guide5);

            em.persist(trip1);
            em.persist(trip2);
            em.persist(trip3);
            em.persist(trip4);
            em.persist(trip5);

            em.getTransaction().commit();
        }
    }

}
