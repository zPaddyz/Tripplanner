package test.dao;

import dat.config.HibernateConfig;
import dat.config.Populate;
import dat.daos.impl.TripDAO;
import dat.dtos.TripDTO;
import dat.entities.Trip;
import org.junit.jupiter.api.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TripDAOTest {

    private static EntityManagerFactory emf;
    private TripDAO tripDAO;

    @BeforeAll
    public void setUp() {
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        tripDAO = TripDAO.getInstance(emf);
        Populate.populateDataWithRequest();

        /*emf = HibernateConfig.getEntityManagerFactoryForTest();
         // "test-pu" must be defined in persistence.xml
        tripDAO = TripDAO.getInstance(emf);

        // Prepare test data
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // Create test trips
            Trip trip1 = new Trip("John Doe", LocalDate.of(2024, 10, 15), LocalTime.of(10, 30), LocalTime.of(11, 30), -122.4194, 37.7749, 150.0);
            Trip trip2 = new Trip("Jane Smith", LocalDate.of(2024, 11, 20), LocalTime.of(14, 0), LocalTime.of(15, 0), -73.935242, 40.730610, 200.0);

            // Persist test trips
            em.persist(trip1);
            em.persist(trip2);

            em.getTransaction().commit();*/
        }

    @Test
    public void testCreateTrip() {
        TripDTO newTripDTO = new TripDTO("Trip to canada", LocalDate.of(2024, 12, 1), LocalTime.of(9, 0), LocalTime.of(10, 0), -118.2437, 34.0522, 250.0, Trip.Category.snow);
        TripDTO createdTrip = tripDAO.create(newTripDTO);


        assertNotNull(createdTrip);
        assertEquals("Trip to canada", createdTrip.getName());
        assertEquals(LocalDate.of(2024, 12, 1), createdTrip.getDate());
        assertEquals(LocalTime.of(9, 0), createdTrip.getStartTime());
        assertEquals(250.0, createdTrip.getPrice());
    }

    @Test
    public void testGetByIDTrip() {
        TripDTO tripDTO = tripDAO.getByID(3);
        assertNotNull(tripDTO);
        assertEquals("Tivoli", tripDTO.getName());
        assertEquals(LocalDate.of(2024, 11, 7), tripDTO.getDate());
        assertEquals(LocalTime.of(9, 30), tripDTO.getStartTime());
    }

    @Test
    public void testUpdateTrip() {
        TripDTO updatedTripInfo = new TripDTO();
        updatedTripInfo.setName("Testy McTestface");
        updatedTripInfo.setDate(LocalDate.of(2024, 10, 15));
        updatedTripInfo.setStartTime(LocalTime.of(11, 0));
        updatedTripInfo.setEndTime(LocalTime.of(12, 0));
        updatedTripInfo.setLongitude(-120.4194);
        updatedTripInfo.setLatitude(37.7749);
        updatedTripInfo.setPrice(999.0);

        TripDTO updatedTrip = tripDAO.update(1, updatedTripInfo);
        assertNotNull(updatedTrip);
        assertEquals("Testy McTestface", updatedTrip.getName());
        assertEquals(LocalDate.of(2024, 10, 15), updatedTrip.getDate());
        assertEquals(LocalTime.of(11, 0), updatedTrip.getStartTime());
        assertEquals(999.0, updatedTrip.getPrice());
    }

    @Test
    public void testGetAllAppointments() {
        List<TripDTO> appointments = tripDAO.getAll();
        assertNotNull(appointments);
        assertFalse(appointments.isEmpty());
    }

    @Test
    public void testDeleteAppointment() {
        TripDTO appointmentDTO = tripDAO.getByID(2);
        assertNotNull(appointmentDTO);
        tripDAO.delete(2);
        appointmentDTO = tripDAO.getByID(2);
        assertNull(appointmentDTO);
    }

    @Test
    public void testValidatePrimaryKey() {
        boolean exists = tripDAO.validatePrimaryKey(1);
        assertTrue(exists);

        boolean nonExistent = tripDAO.validatePrimaryKey(999);
        assertFalse(nonExistent);
    }

    @AfterAll
    public void tearDown() {
        if (emf != null) {
            emf.close();
        }
    }
}
