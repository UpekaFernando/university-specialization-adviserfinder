package com.university.advisorfinder.bdd.steps;

import com.university.advisorfinder.dto.StudentRegistrationDTO;
import com.university.advisorfinder.model.Student;
import com.university.advisorfinder.repository.StudentRepository;
import com.university.advisorfinder.service.StudentService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class StudentRegistrationSteps {

    @Autowired
    private StudentService studentService;
    
    @Autowired
    private StudentRepository studentRepository;
    
    private StudentRegistrationDTO registrationDTO;
    private Student registeredStudent;
    private Exception caughtException;
    private String actualMessage;

    @Given("the university advisor finder system is available")
    public void theUniversityAdvisorFinderSystemIsAvailable() {
        // System is running - Spring Boot context loaded
        assertNotNull(studentService, "Student service should be available");
    }

    @And("the student registration service is running")
    public void theStudentRegistrationServiceIsRunning() {
        assertNotNull(studentService, "Student registration service should be running");
        assertNotNull(studentRepository, "Student repository should be available");
    }

    @Given("I am a new student named {string}")
    public void iAmANewStudentNamed(String studentName) {
        registrationDTO = new StudentRegistrationDTO();
        String[] nameParts = studentName.split(" ");
        registrationDTO.setFirstName(nameParts[0]);
        registrationDTO.setLastName(nameParts.length > 1 ? nameParts[1] : "");
    }

    @And("my email address is {string}")
    public void myEmailAddressIs(String email) {
        registrationDTO.setEmail(email);
    }

    @And("my student ID is {string}")
    public void myStudentIdIs(String studentId) {
        registrationDTO.setStudentId(studentId);
    }

    @And("my program is {string}")
    public void myProgramIs(String program) {
        registrationDTO.setProgram(program);
    }

    @Given("a student already exists with email {string}")
    public void aStudentAlreadyExistsWithEmail(String email) {
        // Create existing student for duplicate test
        Student existingStudent = new Student();
        existingStudent.setFirstName("Existing");
        existingStudent.setLastName("Student");
        existingStudent.setEmail(email);
        existingStudent.setStudentId("EXISTING001");
        existingStudent.setProgram("Existing Program");
        studentRepository.save(existingStudent);
    }

    @When("I submit my registration details")
    public void iSubmitMyRegistrationDetails() {
        try {
            caughtException = null;
            registeredStudent = studentService.registerStudent(registrationDTO);
            actualMessage = "confirmation";
        } catch (Exception e) {
            caughtException = e;
            registeredStudent = null;
            actualMessage = e.getMessage();
        }
    }

    @Then("my registration should be successful")
    public void myRegistrationShouldBeSuccessful() {
        assertNull(caughtException, "No exception should be thrown for valid registration");
        assertNotNull(registeredStudent, "Student should be registered successfully");
    }

    @Then("my registration should be {string}")
    public void myRegistrationShouldBe(String expectedResult) {
        if ("successful".equals(expectedResult)) {
            myRegistrationShouldBeSuccessful();
        } else if ("failed".equals(expectedResult)) {
            myRegistrationShouldFail();
        }
    }

    @Then("my registration should fail")
    public void myRegistrationShouldFail() {
        assertNotNull(caughtException, "Exception should be thrown for invalid registration");
        assertNull(registeredStudent, "Student should not be registered");
    }

    @And("I should receive a confirmation")
    public void iShouldReceiveAConfirmation() {
        assertEquals("confirmation", actualMessage, "Should receive confirmation message");
    }

    @And("I should see an error message {string}")
    public void iShouldSeeAnErrorMessage(String expectedMessage) {
        assertNotNull(caughtException, "Exception should contain error message");
        assertEquals(expectedMessage, caughtException.getMessage(), 
                    "Error message should match expected message");
    }

    @And("I should see message {string}")
    public void iShouldSeeMessage(String expectedMessage) {
        if ("confirmation".equals(expectedMessage)) {
            assertEquals("confirmation", actualMessage);
        } else {
            assertEquals(expectedMessage, actualMessage);
        }
    }

    @And("my details should be saved in the system")
    public void myDetailsShouldBeSavedInTheSystem() {
        assertNotNull(registeredStudent, "Student should be saved");
        assertNotNull(registeredStudent.getId(), "Student should have an ID");
        
        // Verify in repository
        assertTrue(studentRepository.existsByEmail(registrationDTO.getEmail()),
                  "Student email should exist in repository");
    }

    @And("my details should not be saved in the system")
    public void myDetailsShouldNotBeSavedInTheSystem() {
        assertNull(registeredStudent, "Student should not be saved");
        
        // Verify NOT in repository (except for duplicate email test)
        if (!caughtException.getMessage().contains("already exists")) {
            assertFalse(studentRepository.existsByEmail(registrationDTO.getEmail()),
                       "Student email should not exist in repository");
        }
    }
}