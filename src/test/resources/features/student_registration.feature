Feature: Student Registration
  As a university student
  I want to register for the advisor finder system
  So that I can find suitable academic advisors

  Background:
    Given the university advisor finder system is available
    And the student registration service is running

  Scenario: Successful student registration with valid email
    Given I am a new student named "John Doe"
    And my email address is "john.doe@university.edu"
    And my student ID is "STU001"
    And my program is "Computer Science"
    When I submit my registration details
    Then my registration should be successful
    And I should receive a confirmation
    And my details should be saved in the system

  Scenario: Registration fails with invalid email format
    Given I am a new student named "Jane Smith" 
    And my email address is "invalid-email-format"
    And my student ID is "STU002"
    And my program is "Mathematics"
    When I submit my registration details
    Then my registration should fail
    And I should see an error message "Invalid email format"
    And my details should not be saved in the system

  Scenario: Registration fails with duplicate email
    Given a student already exists with email "existing@university.edu"
    And I am a new student named "Bob Wilson"
    And my email address is "existing@university.edu"
    And my student ID is "STU003" 
    And my program is "Physics"
    When I submit my registration details
    Then my registration should fail
    And I should see an error message "Email already exists"
    And my details should not be saved in the system

  Scenario Outline: Registration with different email formats
    Given I am a new student named "Test Student"
    And my email address is "<email>"
    And my student ID is "<student_id>"
    And my program is "Engineering"
    When I submit my registration details
    Then my registration should be "<result>"
    And I should see message "<message>"

    Examples:
      | email                    | student_id | result     | message                |
      | valid@university.edu     | STU100     | successful | confirmation           |
      | no-at-symbol             | STU101     | failed     | Invalid email format   |
      | @missing-before.com      | STU102     | failed     | Invalid email format   |
      | missing-after@           | STU103     | failed     | Invalid email format   |
      | good.email@college.edu   | STU104     | successful | confirmation           |