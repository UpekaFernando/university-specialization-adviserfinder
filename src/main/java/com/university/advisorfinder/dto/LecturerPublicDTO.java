package com.university.advisorfinder.dto;

import java.util.Set;

public class LecturerPublicDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String title;
    private String department;
    private String bio;
    private String profileImageUrl;
    private Set<ResearchInterestDTO> researchInterests;
    
    // Constructors
    public LecturerPublicDTO() {}
    
    public LecturerPublicDTO(Long id, String firstName, String lastName, String title, 
                           String department, String bio, String profileImageUrl) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.title = title;
        this.department = department;
        this.bio = bio;
        this.profileImageUrl = profileImageUrl;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
    
    public String getProfileImageUrl() { return profileImageUrl; }
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }
    
    public Set<ResearchInterestDTO> getResearchInterests() { return researchInterests; }
    public void setResearchInterests(Set<ResearchInterestDTO> researchInterests) { this.researchInterests = researchInterests; }
    
    public String getFullName() {
        return firstName + " " + lastName;
    }
}
