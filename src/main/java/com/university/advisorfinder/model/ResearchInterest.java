package com.university.advisorfinder.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "research_interests")
public class ResearchInterest {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Interest name is required")
    @Size(max = 100)
    @Column(name = "name", nullable = false)
    private String name;
    
    @Size(max = 500)
    @Column(name = "description")
    private String description;
    
    @JsonBackReference("category-interests")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private ResearchCategory category;
    
    @JsonBackReference("lecturer-interests")
    @ManyToMany(mappedBy = "researchInterests", fetch = FetchType.LAZY)
    private Set<Lecturer> lecturers = new HashSet<>();
    
    // Constructors
    public ResearchInterest() {}
    
    public ResearchInterest(String name, String description, ResearchCategory category) {
        this.name = name;
        this.description = description;
        this.category = category;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public ResearchCategory getCategory() { return category; }
    public void setCategory(ResearchCategory category) { this.category = category; }
    
    public Set<Lecturer> getLecturers() { return lecturers; }
    public void setLecturers(Set<Lecturer> lecturers) { this.lecturers = lecturers; }
}
