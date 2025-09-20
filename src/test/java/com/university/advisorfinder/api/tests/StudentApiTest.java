package com.university.advisorfinder.api.tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

/**
 * REST Assured API Tests for Student Registration Endpoints
 * Tests POST /api/students, GET /api/students, and validation
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(ApiTestConfiguration.class)
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.jpa.defer-datasource-initialization=true",
    "spring.sql.init.mode=never",
    "logging.level.org.hibernate.SQL=DEBUG"
})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StudentApiTest {

    @LocalServerPort
    private int port;

    @BeforeAll
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    @DisplayName("POST /api/students - Create New Student")
    public void testCreateStudent() {
        // Test data for student creation
        String studentJson = "{" +
            "\"name\": \"John Doe\"," +
            "\"email\": \"john.doe@university.edu\"," +
            "\"studentId\": \"STU001\"," +
            "\"program\": \"Computer Science\"," +
            "\"yearOfStudy\": 2" +
            "}";

        // API Test: Create student via POST request
        given()
            .contentType(ContentType.JSON)
            .body(studentJson)
        .when()
            .post("/api/students")
        .then()
            .statusCode(201)
            .body("name", equalTo("John Doe"))
            .body("email", equalTo("john.doe@university.edu"))
            .body("studentId", equalTo("STU001"))
            .body("program", equalTo("Computer Science"))
            .body("yearOfStudy", equalTo(2))
            .body("id", notNullValue());

        System.out.println("✅ POST /api/students - Student creation test passed");
    }

    @Test
    @DisplayName("POST /api/students - Invalid Email Validation")
    public void testCreateStudentInvalidEmail() {
        // Test data with invalid email
        String invalidStudentJson = "{" +
            "\"name\": \"Jane Smith\"," +
            "\"email\": \"invalid-email-format\"," +
            "\"studentId\": \"STU002\"," +
            "\"program\": \"Mathematics\"," +
            "\"yearOfStudy\": 1" +
            "}";

        // API Test: Validate email format
        given()
            .contentType(ContentType.JSON)
            .body(invalidStudentJson)
        .when()
            .post("/api/students")
        .then()
            .statusCode(400)
            .body("message", containsString("Invalid email format"));

        System.out.println("✅ POST /api/students - Email validation test passed");
    }

    @Test
    @DisplayName("GET /api/students - Retrieve All Students")
    public void testGetAllStudents() {
        // First create a student
        String studentJson = "{" +
            "\"name\": \"Alice Johnson\"," +
            "\"email\": \"alice.johnson@university.edu\"," +
            "\"studentId\": \"STU003\"," +
            "\"program\": \"Engineering\"," +
            "\"yearOfStudy\": 3" +
            "}";

        given()
            .contentType(ContentType.JSON)
            .body(studentJson)
        .when()
            .post("/api/students");

        // API Test: Retrieve all students
        given()
        .when()
            .get("/api/students")
        .then()
            .statusCode(200)
            .body("size()", greaterThan(0))
            .body("[0].name", notNullValue())
            .body("[0].email", notNullValue())
            .body("[0].studentId", notNullValue());

        System.out.println("✅ GET /api/students - Retrieve students test passed");
    }

    @Test
    @DisplayName("GET /api/students/{id} - Retrieve Student by ID")
    public void testGetStudentById() {
        // Create a student first
        String studentJson = "{" +
            "\"name\": \"Bob Wilson\"," +
            "\"email\": \"bob.wilson@university.edu\"," +
            "\"studentId\": \"STU004\"," +
            "\"program\": \"Physics\"," +
            "\"yearOfStudy\": 4" +
            "}";

        Response createResponse = given()
            .contentType(ContentType.JSON)
            .body(studentJson)
        .when()
            .post("/api/students");

        Long studentId = createResponse.jsonPath().getLong("id");

        // API Test: Retrieve student by ID
        given()
        .when()
            .get("/api/students/" + studentId)
        .then()
            .statusCode(200)
            .body("name", equalTo("Bob Wilson"))
            .body("email", equalTo("bob.wilson@university.edu"))
            .body("studentId", equalTo("STU004"))
            .body("program", equalTo("Physics"));

        System.out.println("✅ GET /api/students/{id} - Retrieve by ID test passed");
    }

    @Test
    @DisplayName("DELETE /api/students/{id} - Remove Student")
    public void testDeleteStudent() {
        // Create a student first
        String studentJson = "{" +
            "\"name\": \"Charlie Brown\"," +
            "\"email\": \"charlie.brown@university.edu\"," +
            "\"studentId\": \"STU005\"," +
            "\"program\": \"Chemistry\"," +
            "\"yearOfStudy\": 1" +
            "}";

        Response createResponse = given()
            .contentType(ContentType.JSON)
            .body(studentJson)
        .when()
            .post("/api/students");

        Long studentId = createResponse.jsonPath().getLong("id");

        // API Test: Delete student
        given()
        .when()
            .delete("/api/students/" + studentId)
        .then()
            .statusCode(204);

        // Verify student is deleted
        given()
        .when()
            .get("/api/students/" + studentId)
        .then()
            .statusCode(404);

        System.out.println("✅ DELETE /api/students/{id} - Delete student test passed");
    }
}