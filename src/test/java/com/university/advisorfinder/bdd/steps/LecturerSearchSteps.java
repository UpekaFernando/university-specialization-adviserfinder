package com.university.advisorfinder.bdd.steps;

import com.university.advisorfinder.model.Lecturer;
import com.university.advisorfinder.model.ResearchInterest;
import com.university.advisorfinder.repository.LecturerRepository;
import com.university.advisorfinder.service.LecturerService;
import com.university.advisorfinder.service.ResearchService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import io.cucumber.datatable.DataTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class LecturerSearchSteps {

    @Autowired
    private LecturerService lecturerService;
    
    @Autowired
    private LecturerRepository lecturerRepository;
    
    @Autowired
    private ResearchService researchService;
    
    private List<String> searchInterests;
    private List<Lecturer> searchResults;
    private String searchMessage;

    @And("the lecturer search service is running")
    public void theLecturerSearchServiceIsRunning() {
        assertNotNull(lecturerService, "Lecturer service should be running");
        assertNotNull(lecturerRepository, "Lecturer repository should be available");
    }

    @And("the following lecturers exist in the system:")
    public void theFollowingLecturersExistInTheSystem(DataTable dataTable) {
        List<Map<String, String>> lecturerData = dataTable.asMaps(String.class, String.class);
        
        for (Map<String, String> row : lecturerData) {
            Lecturer lecturer = new Lecturer();
            lecturer.setFirstName(row.get("name").split(" ")[1]); // Dr. Alice -> Alice
            lecturer.setLastName(row.get("name").split(" ")[2]);   // Johnson
            lecturer.setTitle(row.get("name").split(" ")[0]);      // Dr.
            lecturer.setDepartment(row.get("department"));
            lecturer.setEmail(lecturer.getFirstName().toLowerCase() + "@university.edu");
            
            // Create research interests
            String[] interests = row.get("research_interests").split(",");
            Set<ResearchInterest> researchInterests = new HashSet<>();
            
            for (String interestName : interests) {
                String trimmedName = interestName.trim();
                ResearchInterest interest = researchService.findOrCreateInterest(trimmedName, "Research interest: " + trimmedName);
                researchInterests.add(interest);
            }
            
            lecturer.setResearchInterests(researchInterests);
            lecturerRepository.save(lecturer);
        }
    }

    @Given("I am a student looking for advisors")
    public void iAmAStudentLookingForAdvisors() {
        // Student context is established
        searchInterests = new ArrayList<>();
        searchResults = new ArrayList<>();
        searchMessage = "";
    }

    @When("I search for lecturers with research interests:")
    public void iSearchForLecturersWithResearchInterests(DataTable dataTable) {
        searchInterests = dataTable.asList(String.class);
        performSearch();
    }

    @When("I search for lecturers with no research interests specified")
    public void iSearchForLecturersWithNoResearchInterestsSpecified() {
        searchInterests = new ArrayList<>(); // Empty list
        performSearch();
    }

    @When("I search for lecturers with research interests {string}")
    public void iSearchForLecturersWithResearchInterests(String interests) {
        if ("Nonexistent Interest".equals(interests)) {
            searchInterests = Arrays.asList("Nonexistent Interest");
        } else {
            searchInterests = Arrays.asList(interests.split(","));
        }
        performSearch();
    }

    private void performSearch() {
        try {
            searchResults = lecturerService.searchLecturersByInterests(searchInterests);
            if (searchResults.isEmpty()) {
                searchMessage = "no matches found";
            } else {
                searchMessage = "matches found";
            }
        } catch (Exception e) {
            searchResults = new ArrayList<>();
            searchMessage = e.getMessage();
        }
    }

    @Then("I should find the following lecturers:")
    public void iShouldFindTheFollowingLecturers(DataTable dataTable) {
        List<String> expectedLecturers = dataTable.asList(String.class);
        
        assertEquals(expectedLecturers.size(), searchResults.size(), 
                    "Number of found lecturers should match expected");
        
        Set<String> actualNames = searchResults.stream()
            .map(lecturer -> lecturer.getTitle() + " " + lecturer.getFirstName() + " " + lecturer.getLastName())
            .collect(Collectors.toSet());
        
        for (String expectedName : expectedLecturers) {
            assertTrue(actualNames.contains(expectedName), 
                      "Should find lecturer: " + expectedName);
        }
    }

    @Then("I should find no lecturers")
    public void iShouldFindNoLecturers() {
        assertTrue(searchResults.isEmpty(), "Search results should be empty");
    }

    @Then("I should find {string} lecturers")
    public void iShouldFindLecturers(String expectedCount) {
        int expected = Integer.parseInt(expectedCount);
        assertEquals(expected, searchResults.size(), 
                    "Should find exactly " + expected + " lecturers");
    }

    @And("the search results should not be empty")
    public void theSearchResultsShouldNotBeEmpty() {
        assertFalse(searchResults.isEmpty(), "Search results should not be empty");
    }

    @And("the search results should be empty")
    public void theSearchResultsShouldBeEmpty() {
        assertTrue(searchResults.isEmpty(), "Search results should be empty");
    }

    @And("the search results should contain {int} lecturers")
    public void theSearchResultsShouldContainLecturers(int expectedCount) {
        assertEquals(expectedCount, searchResults.size(), 
                    "Search results should contain exactly " + expectedCount + " lecturers");
    }

    @And("each lecturer should have at least one matching research interest")
    public void eachLecturerShouldHaveAtLeastOneMatchingResearchInterest() {
        for (Lecturer lecturer : searchResults) {
            boolean hasMatchingInterest = lecturer.getResearchInterests().stream()
                .anyMatch(interest -> searchInterests.contains(interest.getName()));
            
            assertTrue(hasMatchingInterest, 
                      "Lecturer " + lecturer.getFirstName() + " " + lecturer.getLastName() + 
                      " should have at least one matching research interest");
        }
    }

    @And("I should see a message indicating no matches found")
    public void iShouldSeeAMessageIndicatingNoMatchesFound() {
        assertEquals("no matches found", searchMessage, 
                    "Should see 'no matches found' message");
    }

    @And("the results should include {string}")
    public void theResultsShouldInclude(String expectedLecturers) {
        if ("none".equals(expectedLecturers)) {
            assertTrue(searchResults.isEmpty(), "Results should be empty");
        } else {
            String[] lecturerNames = expectedLecturers.split(",");
            Set<String> actualNames = searchResults.stream()
                .map(lecturer -> lecturer.getTitle() + " " + lecturer.getFirstName() + " " + lecturer.getLastName())
                .collect(Collectors.toSet());
            
            for (String expectedName : lecturerNames) {
                assertTrue(actualNames.contains(expectedName.trim()), 
                          "Results should include: " + expectedName.trim());
            }
        }
    }
}