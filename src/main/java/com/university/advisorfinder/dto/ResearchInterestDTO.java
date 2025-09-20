package com.university.advisorfinder.dto;

public class ResearchInterestDTO {
    private Long id;
    private String name;
    private String description;
    private String categoryName;
    
    // Constructors
    public ResearchInterestDTO() {}
    
    public ResearchInterestDTO(Long id, String name, String description, String categoryName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.categoryName = categoryName;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
}
