package com.university.advisorfinder.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.BatchSize;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "research_categories")
public class ResearchCategory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Category name is required")
    @Size(max = 100)
    @Column(name = "name", nullable = false, unique = true)
    private String name;
    
    @Size(max = 500)
    @Column(name = "description")
    private String description;
    
    @JsonManagedReference("category-interests")
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @BatchSize(size = 10)
    private Set<ResearchInterest> researchInterests = new HashSet<>();
    
    // Constructors
    public ResearchCategory() {}
    
    public ResearchCategory(String name, String description) {
        this.name = name;
        this.description = description;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Set<ResearchInterest> getResearchInterests() { return researchInterests; }
    public void setResearchInterests(Set<ResearchInterest> researchInterests) { this.researchInterests = researchInterests; }
}
