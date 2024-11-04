package test.routes;

import dat.config.ApplicationConfig;
import dat.config.HibernateConfig;
import dat.config.Populate;
import dat.daos.impl.GuideDAO;
import dat.daos.impl.UserDAO;
import dat.dtos.TripDTO;
import dat.entities.Trip;
import dat.security.controllers.SecurityController;
import dat.security.daos.SecurityDAO;
import dat.security.exceptions.ValidationException;
import dk.bugelhartmann.UserDTO;
import io.javalin.Javalin;
import io.restassured.common.mapper.TypeRef;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.containsInAnyOrder;

/**
 * Guide Route Test class
 * Purpose: To test the routes we have in our GuideRoute class
 */

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TripRouteTest {

    private static Javalin app;
    private static EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryForTest();
    private final static SecurityController securityController = SecurityController.getInstance();
    private final static SecurityDAO securityDAO = new SecurityDAO(emf);
    private static String BASE_URL = "http://localhost:7070/api";
    private static Populate populator = new Populate(emf);

    private static UserDTO userDTO, adminDTO;
    private static String userToken, adminToken;

    /**
     * Starts the server for the tests
     */
    @BeforeAll
    void init() {
        app = ApplicationConfig.startServer(7070);
    }

    /**
     * Sets up the data we need for our tests, by using various Populate methods
     * Sets up the user and admin tokens, which is required for authorization on the endpoints
     */
    @BeforeEach
    void setUp() {
        Populate.populateDataWithRequest();

        UserDTO[] users = Populate.populateUsers(emf);
        userDTO = users[0];
        adminDTO = users[1];

        try {
            UserDTO verifiedUser = securityDAO.getVerifiedUser(userDTO.getUsername(), userDTO.getPassword());
            UserDTO verifiedAdmin = securityDAO.getVerifiedUser(adminDTO.getUsername(), adminDTO.getPassword());
            userToken = "Bearer " + securityController.createToken(verifiedUser);
            adminToken = "Bearer " + securityController.createToken(verifiedAdmin);
        } catch (ValidationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Cleans up data
     */
    @AfterEach
    void tearDown() {
        populator.cleanUpData();
    }

    @Test
    void testGetGuides() {
        List<TripDTO> tripDTOS =
                given()
                        .header("Authorization", userToken)
                        .when()
                        .get(BASE_URL + "/trips")
                        .then()
                        .statusCode(200)
                        .body("size()", is(5))
                        .log().all()
                        .extract()
                        .as(new TypeRef<List<TripDTO>>() {
                        });

        assertThat(tripDTOS.size(), is(5));
        assertThat(tripDTOS.stream()
                        .map(TripDTO::getName)
                        .collect(Collectors.toList()),
                hasItems("USA Trip", "BonBon Land"));
    }

    @Test
    void testGetGuideById() {
        int tripId = 1;
        TripDTO tripDTO =
                given()
                        .header("Authorization", userToken)
                        .when()
                        .get(BASE_URL + "/trips/" + tripId)
                        .then()
                        .statusCode(200)
                        .log().all()
                        .extract()
                        .as(TripDTO.class);

        assertThat(tripDTO.getName(), is("USA Trip"));
    }

    @Test
    void testGetTripsByFilter() {
        List<TripDTO> tripDTOS =
                given()
                        .header("Authorization", userToken)
                        .when()
                        .get(BASE_URL + "/trips/filter/beach")
                        .then()
                        .statusCode(200)
                        .log().all()
                        .extract()
                        .as(new TypeRef<List<TripDTO>>() {
                        });

        assertThat(tripDTOS.size(), is(1));
        assertThat(tripDTOS.get(0).getName(), is("BonBon Land"));
    }

    @Test
    void testUpdateTrip() {
        int tripId = 1; // Replace with the actual ID

        TripDTO updatedtrip = new TripDTO("Tur til testland", LocalDate.of(2024, 10, 15), LocalTime.of(9, 30), LocalTime.of(10, 30),-122.4194, 37.7749, 150.0, Trip.Category.beach);

        TripDTO tripDTO =
                given()
                        .header("Authorization", adminToken)
                        .contentType("application/json")
                        .body(updatedtrip)
                        .when()
                        .put(BASE_URL + "/trips/" + tripId)
                        .then()
                        .log().all()
                        .statusCode(200)
                        .extract()
                        .as(TripDTO.class);

        assertThat(tripDTO.getName(), is("Tur til testland"));
    }

    @Test
    void testDeleteTrip() {
        int tripId = 2;

        given()
                .header("Authorization", adminToken)
                .when()
                .delete(BASE_URL + "/trips/" + tripId)
                .then()
                .statusCode(204);
    }

}

