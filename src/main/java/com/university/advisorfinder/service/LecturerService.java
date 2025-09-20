package com.university.advisorfinder.service;

import com.university.advisorfinder.dto.LecturerContactDTO;
import com.university.advisorfinder.dto.LecturerPublicDTO;
import com.university.advisorfinder.dto.ResearchInterestDTO;
import com.university.advisorfinder.model.Lecturer;
import com.university.advisorfinder.model.ResearchInterest;
import com.university.advisorfinder.repository.LecturerRepository;
import com.university.advisorfinder.repository.ResearchInterestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class LecturerService {
    
    @Autowired
    private LecturerRepository lecturerRepository;
    
    @Autowired
    private ResearchInterestRepository researchInterestRepository;
    
    public List<LecturerPublicDTO> getAllLecturersPublic() {
        return lecturerRepository.findAll().stream()
                .map(this::convertToPublicDTO)
                .collect(Collectors.toList());
    }
    
    public Optional<LecturerContactDTO> getLecturerContact(Long id) {
        return lecturerRepository.findById(id)
                .map(this::convertToContactDTO);
    }
    
    public List<LecturerPublicDTO> searchLecturersByKeyword(String keyword) {
        return lecturerRepository.searchByKeyword(keyword).stream()
                .map(this::convertToPublicDTO)
                .collect(Collectors.toList());
    }
    
    public List<LecturerPublicDTO> findLecturersByResearchInterests(List<Long> interestIds) {
        return lecturerRepository.findByResearchInterestIds(interestIds).stream()
                .map(this::convertToPublicDTO)
                .collect(Collectors.toList());
    }
    
    public List<LecturerPublicDTO> findLecturersByCategory(Long categoryId) {
        return lecturerRepository.findByResearchCategoryId(categoryId).stream()
                .map(this::convertToPublicDTO)
                .collect(Collectors.toList());
    }
    
    public List<LecturerPublicDTO> findLecturersByDepartment(String department) {
        return lecturerRepository.findByDepartmentContainingIgnoreCase(department).stream()
                .map(this::convertToPublicDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Searches for lecturers by their research interest names.
     * TDD REFACTOR PHASE: Enhanced with comprehensive validation and documentation.
     * 
     * @param interestNames List of research interest names to search for
     * @return List of lecturers who have matching research interests, empty list if none found
     * @throws IllegalArgumentException if any interest name is null or empty
     */
    public List<Lecturer> searchLecturersByInterests(List<String> interestNames) {
        // Input validation - return empty for null/empty input
        if (isNullOrEmpty(interestNames)) {
            return new ArrayList<>();
        }
        
        // Validate all interest names are non-empty
        validateInterestNames(interestNames);
        
        // Delegate to repository for database query
        return lecturerRepository.findByResearchInterests_NameIn(interestNames);
    }
    
    /**
     * Checks if the interest names list is null or empty.
     * @param interestNames the list to check
     * @return true if null or empty
     */
    private boolean isNullOrEmpty(List<String> interestNames) {
        return interestNames == null || interestNames.isEmpty();
    }
    
    /**
     * Validates that all interest names are non-null and non-empty.
     * @param interestNames the list of interest names to validate
     * @throws IllegalArgumentException if any name is null or empty
     */
    private void validateInterestNames(List<String> interestNames) {
        for (String interestName : interestNames) {
            if (interestName == null || interestName.trim().isEmpty()) {
                throw new IllegalArgumentException("Interest names cannot be null or empty");
            }
        }
    }
    
    public Lecturer saveLecturer(Lecturer lecturer) {
        return lecturerRepository.save(lecturer);
    }
    
    public Optional<Lecturer> findById(Long id) {
        return lecturerRepository.findById(id);
    }
    
    public Optional<Lecturer> findByEmail(String email) {
        return lecturerRepository.findByEmail(email);
    }
    
    public void deleteLecturer(Long id) {
        lecturerRepository.deleteById(id);
    }
    
    private LecturerPublicDTO convertToPublicDTO(Lecturer lecturer) {
        LecturerPublicDTO dto = new LecturerPublicDTO(
            lecturer.getId(),
            lecturer.getFirstName(),
            lecturer.getLastName(),
            lecturer.getTitle(),
            lecturer.getDepartment(),
            lecturer.getBio(),
            lecturer.getProfileImageUrl()
        );
        
        Set<ResearchInterestDTO> interestDTOs = lecturer.getResearchInterests().stream()
                .map(this::convertToResearchInterestDTO)
                .collect(Collectors.toSet());
        dto.setResearchInterests(interestDTOs);
        
        return dto;
    }
    
    private LecturerContactDTO convertToContactDTO(Lecturer lecturer) {
        return new LecturerContactDTO(
            lecturer.getId(),
            lecturer.getFirstName(),
            lecturer.getLastName(),
            lecturer.getEmail(),
            lecturer.getPhone(),
            lecturer.getOfficeLocation(),
            lecturer.getOfficeHours(),
            lecturer.getTitle(),
            lecturer.getDepartment()
        );
    }
    
    private ResearchInterestDTO convertToResearchInterestDTO(ResearchInterest interest) {
        return new ResearchInterestDTO(
            interest.getId(),
            interest.getName(),
            interest.getDescription(),
            interest.getCategory().getName()
        );
    }
    
    public void initializeSampleLecturers() {
        if (lecturerRepository.count() == 0) {
            createSampleLecturers();
        }
    }
    
    private void createSampleLecturers() {
        // Create sample lecturers with research interests
        List<ResearchInterest> allInterests = researchInterestRepository.findAll();
        
        // Dr. John Smith - AI and Software Engineering
        Lecturer drSmith = new Lecturer();
        drSmith.setTitle("Dr.");
        drSmith.setFirstName("John");
        drSmith.setLastName("Smith");
        drSmith.setDepartment("Computer Science");
        drSmith.setEmail("john.smith@university.edu");
        drSmith.setPhone("+1-555-0101");
        drSmith.setBio("Dr. Smith is a Professor of Computer Science specializing in Artificial Intelligence and Machine Learning. He has over 15 years of experience in AI research and has published numerous papers in top-tier conferences.");
        drSmith.setOfficeLocation("CS Building, Room 301");
        drSmith.setOfficeHours("Mon/Wed 2-4 PM, Fri 10-12 PM");
        
        Set<ResearchInterest> smithInterests = new HashSet<>();
        allInterests.stream()
            .filter(interest -> interest.getName().contains("Artificial Intelligence") || 
                               interest.getName().contains("Software Engineering"))
            .forEach(smithInterests::add);
        drSmith.setResearchInterests(smithInterests);
        
        // Dr. Sarah Johnson - Data Science and Cybersecurity
        Lecturer drJohnson = new Lecturer();
        drJohnson.setTitle("Dr.");
        drJohnson.setFirstName("Sarah");
        drJohnson.setLastName("Johnson");
        drJohnson.setDepartment("Computer Science");
        drJohnson.setEmail("sarah.johnson@university.edu");
        drJohnson.setPhone("+1-555-0102");
        drJohnson.setBio("Dr. Johnson is an Associate Professor specializing in Data Science and Cybersecurity. She leads research projects in big data analytics and information security.");
        drJohnson.setOfficeLocation("CS Building, Room 205");
        drJohnson.setOfficeHours("Tue/Thu 1-3 PM");
        
        Set<ResearchInterest> johnsonInterests = new HashSet<>();
        allInterests.stream()
            .filter(interest -> interest.getName().contains("Data Science") || 
                               interest.getName().contains("Cybersecurity"))
            .forEach(johnsonInterests::add);
        drJohnson.setResearchInterests(johnsonInterests);
        
        // Dr. Michael Brown - Mechanical Engineering
        Lecturer drBrown = new Lecturer();
        drBrown.setTitle("Dr.");
        drBrown.setFirstName("Michael");
        drBrown.setLastName("Brown");
        drBrown.setDepartment("Mechanical Engineering");
        drBrown.setEmail("michael.brown@university.edu");
        drBrown.setPhone("+1-555-0103");
        drBrown.setBio("Dr. Brown is a Professor of Mechanical Engineering with expertise in robotics, manufacturing systems, and energy systems. He has industry experience and strong ties with engineering companies.");
        drBrown.setOfficeLocation("Engineering Building, Room 401");
        drBrown.setOfficeHours("Mon/Wed/Fri 9-11 AM");
        
        Set<ResearchInterest> brownInterests = new HashSet<>();
        allInterests.stream()
            .filter(interest -> interest.getName().contains("Mechanical Engineering"))
            .forEach(brownInterests::add);
        drBrown.setResearchInterests(brownInterests);
        
        // Dr. Emily Davis - Finance and Marketing
        Lecturer drDavis = new Lecturer();
        drDavis.setTitle("Dr.");
        drDavis.setFirstName("Emily");
        drDavis.setLastName("Davis");
        drDavis.setDepartment("Business Administration");
        drDavis.setEmail("emily.davis@university.edu");
        drDavis.setPhone("+1-555-0104");
        drDavis.setBio("Dr. Davis is an Associate Professor in the Business School with specializations in Finance and Digital Marketing. She has worked extensively with startups and established companies on financial strategy and marketing campaigns.");
        drDavis.setOfficeLocation("Business Building, Room 302");
        drDavis.setOfficeHours("Tue/Thu 10 AM-12 PM");
        
        Set<ResearchInterest> davisInterests = new HashSet<>();
        allInterests.stream()
            .filter(interest -> interest.getName().contains("Finance") || 
                               interest.getName().contains("Marketing"))
            .forEach(davisInterests::add);
        drDavis.setResearchInterests(davisInterests);
        
        // Dr. Robert Wilson - Biology and Chemistry
        Lecturer drWilson = new Lecturer();
        drWilson.setTitle("Dr.");
        drWilson.setFirstName("Robert");
        drWilson.setLastName("Wilson");
        drWilson.setDepartment("Life Sciences");
        drWilson.setEmail("robert.wilson@university.edu");
        drWilson.setPhone("+1-555-0105");
        drWilson.setBio("Dr. Wilson is a Professor in the Life Sciences department with research focus on molecular biology and biochemistry. His lab studies protein interactions and drug discovery.");
        drWilson.setOfficeLocation("Science Building, Room 501");
        drWilson.setOfficeHours("Mon/Wed 3-5 PM");
        
        Set<ResearchInterest> wilsonInterests = new HashSet<>();
        allInterests.stream()
            .filter(interest -> interest.getName().contains("Biology") || 
                               interest.getName().contains("Chemistry"))
            .forEach(wilsonInterests::add);
        drWilson.setResearchInterests(wilsonInterests);
        
        // Save all lecturers
        lecturerRepository.save(drSmith);
        lecturerRepository.save(drJohnson);
        lecturerRepository.save(drBrown);
        lecturerRepository.save(drDavis);
        lecturerRepository.save(drWilson);
    }
}
