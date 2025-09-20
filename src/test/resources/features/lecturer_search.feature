Feature: Lecturer Search by Research Interests
  As a university student
  I want to search for lecturers by their research interests
  So that I can find advisors who match my academic focus

  Background:
    Given the university advisor finder system is available
    And the lecturer search service is running
    And the following lecturers exist in the system:
      | name           | department        | research_interests                    |
      | Dr. Alice Johnson | Computer Science | Machine Learning, AI, Data Science   |
      | Dr. Bob Chen      | Mathematics      | Statistics, Probability, Data Analysis|
      | Dr. Carol Davis   | Physics          | Quantum Computing, Theoretical Physics|
      | Dr. David Brown   | Computer Science | Software Engineering, Web Development |

  Scenario: Successfully find lecturers with matching research interests
    Given I am a student looking for advisors
    When I search for lecturers with research interests:
      | Machine Learning |
      | Data Science     |
    Then I should find the following lecturers:
      | Dr. Alice Johnson |
    And the search results should not be empty
    And each lecturer should have at least one matching research interest

  Scenario: Search returns empty when no lecturers match interests
    Given I am a student looking for advisors
    When I search for lecturers with research interests:
      | Underwater Basket Weaving |
      | Ancient Alien Studies     |
    Then I should find no lecturers
    And the search results should be empty
    And I should see a message indicating no matches found

  Scenario: Search with empty criteria returns empty results
    Given I am a student looking for advisors
    When I search for lecturers with no research interests specified
    Then I should find no lecturers
    And the search results should be empty

  Scenario: Search with multiple interests finds multiple lecturers
    Given I am a student looking for advisors
    When I search for lecturers with research interests:
      | Machine Learning    |
      | Statistics          |
      | Software Engineering|
    Then I should find the following lecturers:
      | Dr. Alice Johnson |
      | Dr. Bob Chen      |
      | Dr. David Brown   |
    And the search results should contain 3 lecturers
    And each lecturer should have at least one matching research interest

  Scenario Outline: Search with different research interest combinations
    Given I am a student looking for advisors
    When I search for lecturers with research interests "<interests>"
    Then I should find "<expected_count>" lecturers
    And the results should include "<expected_lecturers>"

    Examples:
      | interests                          | expected_count | expected_lecturers        |
      | Machine Learning                   | 1              | Dr. Alice Johnson         |
      | Statistics,Probability             | 1              | Dr. Bob Chen              |
      | Software Engineering               | 1              | Dr. David Brown           |
      | AI,Quantum Computing               | 2              | Dr. Alice Johnson,Dr. Carol Davis |
      | Nonexistent Interest               | 0              | none                      |