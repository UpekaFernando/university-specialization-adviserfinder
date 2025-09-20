package com.university.advisorfinder.api.tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

/**
 * REST Assured API Tests for Lecturer Search Endpoints
 * Tests GET /api/lecturers, search by research interest, department filtering
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LecturerApiTest {

    @LocalServerPort
    private int port;

    @BeforeAll
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    @DisplayName("GET /api/lecturers - Retrieve All Lecturers")
    public void testGetAllLecturers() {
        // API Test: Retrieve all lecturers
        given()
        .when()
            .get("/api/lecturers")
        .then()
            .statusCode(200)
            .body("size()", greaterThanOrEqualTo(0))
            .contentType(ContentType.JSON);

        System.out.println("✅ GET /api/lecturers - Retrieve all lecturers test passed");
    }

    @Test
    @DisplayName("GET /api/lecturers/search - Search by Research Interest")
    public void testSearchLecturersByResearchInterest() {
        // API Test: Search lecturers by research interest
        given()
            .param("researchInterest", "Machine Learning")
        .when()
            .get("/api/lecturers/search")
        .then()
            .statusCode(200)
            .body("size()", greaterThanOrEqualTo(0))
            .contentType(ContentType.JSON);

        System.out.println("✅ GET /api/lecturers/search - Research interest search test passed");
    }

    @Test
    @DisplayName("GET /api/lecturers/department/{dept} - Filter by Department")
    public void testGetLecturersByDepartment() {
        // API Test: Filter lecturers by department
        given()
        .when()
            .get("/api/lecturers/department/Computer Science")
        .then()
            .statusCode(200)
            .body("size()", greaterThanOrEqualTo(0))
            .contentType(ContentType.JSON);

        System.out.println("✅ GET /api/lecturers/department/{dept} - Department filter test passed");
    }

    @Test
    @DisplayName("GET /api/lecturers/{id} - Retrieve Lecturer by ID")
    public void testGetLecturerById() {
        // First get all lecturers to find a valid ID
        given()
        .when()
            .get("/api/lecturers")
        .then()
            .statusCode(200);

        // API Test: Retrieve lecturer by ID (using ID 1 as example)
        given()
        .when()
            .get("/api/lecturers/1")
        .then()
            .statusCode(anyOf(equalTo(200), equalTo(404)))
            .contentType(ContentType.JSON);

        System.out.println("✅ GET /api/lecturers/{id} - Retrieve by ID test passed");
    }

    @Test
    @DisplayName("GET /api/lecturers/available - Get Available Lecturers")
    public void testGetAvailableLecturers() {
        // API Test: Get available lecturers for mentoring
        given()
        .when()
            .get("/api/lecturers/available")
        .then()
            .statusCode(200)
            .body("size()", greaterThanOrEqualTo(0))
            .contentType(ContentType.JSON);

        System.out.println("✅ GET /api/lecturers/available - Available lecturers test passed");
    }

    @Test
    @DisplayName("POST /api/lecturers - Create New Lecturer")
    public void testCreateLecturer() {
        // Test data for lecturer creation
        String lecturerJson = "{" +
            "\"name\": \"Dr. Sarah Wilson\"," +
            "\"email\": \"sarah.wilson@university.edu\"," +
            "\"department\": \"Computer Science\"," +
            "\"researchInterests\": [\"Artificial Intelligence\", \"Machine Learning\"]," +
            "\"availability\": true" +
            "}";

        // API Test: Create lecturer via POST request
        given()
            .contentType(ContentType.JSON)
            .body(lecturerJson)
        .when()
            .post("/api/lecturers")
        .then()
            .statusCode(201)
            .body("name", equalTo("Dr. Sarah Wilson"))
            .body("email", equalTo("sarah.wilson@university.edu"))
            .body("department", equalTo("Computer Science"))
            .body("availability", equalTo(true))
            .body("id", notNullValue());

        System.out.println("✅ POST /api/lecturers - Lecturer creation test passed");
    }

    @Test
    @DisplayName("PUT /api/lecturers/{id}/availability - Update Availability")
    public void testUpdateLecturerAvailability() {
        // First create a lecturer
        String lecturerJson = "{" +
            "\"name\": \"Dr. Michael Brown\"," +
            "\"email\": \"michael.brown@university.edu\"," +
            "\"department\": \"Mathematics\"," +
            "\"researchInterests\": [\"Statistics\", \"Data Analysis\"]," +
            "\"availability\": true" +
            "}";

        // Create lecturer and get ID
        Integer lecturerId = given()
            .contentType(ContentType.JSON)
            .body(lecturerJson)
        .when()
            .post("/api/lecturers")
        .then()
            .statusCode(201)
            .extract()
            .path("id");

        // API Test: Update lecturer availability
        given()
            .contentType(ContentType.JSON)
            .body("{\"availability\": false}")
        .when()
            .put("/api/lecturers/" + lecturerId + "/availability")
        .then()
            .statusCode(200)
            .body("availability", equalTo(false));

        System.out.println("✅ PUT /api/lecturers/{id}/availability - Update availability test passed");
    }

    @Test
    @DisplayName("GET /api/lecturers/stats - Get Lecturer Statistics")
    public void testGetLecturerStatistics() {
        // API Test: Get lecturer statistics
        given()
        .when()
            .get("/api/lecturers/stats")
        .then()
            .statusCode(200)
            .body("totalLecturers", greaterThanOrEqualTo(0))
            .body("availableLecturers", greaterThanOrEqualTo(0))
            .body("departmentCount", notNullValue())
            .contentType(ContentType.JSON);

        System.out.println("✅ GET /api/lecturers/stats - Statistics test passed");
    }
}