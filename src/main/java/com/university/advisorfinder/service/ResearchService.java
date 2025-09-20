package com.university.advisorfinder.service;

import com.university.advisorfinder.model.ResearchCategory;
import com.university.advisorfinder.model.ResearchInterest;
import com.university.advisorfinder.repository.ResearchCategoryRepository;
import com.university.advisorfinder.repository.ResearchInterestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ResearchService {
    
    @Autowired
    private ResearchCategoryRepository categoryRepository;
    
    @Autowired
    private ResearchInterestRepository interestRepository;
    
    // Category methods
    public List<ResearchCategory> getAllCategories() {
        return categoryRepository.findAll();
    }
    
    public Optional<ResearchCategory> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }
    
    public ResearchCategory saveCategory(ResearchCategory category) {
        return categoryRepository.save(category);
    }
    
    // Interest methods
    public List<ResearchInterest> getAllInterests() {
        return interestRepository.findAll();
    }
    
    public List<ResearchInterest> getInterestsByCategory(Long categoryId) {
        return interestRepository.findByCategoryId(categoryId);
    }
    
    public List<ResearchInterest> searchInterestsByKeyword(String keyword) {
        return interestRepository.searchByKeyword(keyword);
    }
    
    public Optional<ResearchInterest> getInterestById(Long id) {
        return interestRepository.findById(id);
    }
    
    public ResearchInterest saveInterest(ResearchInterest interest) {
        return interestRepository.save(interest);
    }
    
    public ResearchInterest findOrCreateInterest(String name, String description) {
        Optional<ResearchInterest> existing = interestRepository.findByName(name);
        if (existing.isPresent()) {
            return existing.get();
        }
        
        // Create new interest with default category if no category exists
        ResearchCategory defaultCategory = getOrCreateDefaultCategory();
        ResearchInterest newInterest = new ResearchInterest(name, description, defaultCategory);
        return interestRepository.save(newInterest);
    }
    
    private ResearchCategory getOrCreateDefaultCategory() {
        Optional<ResearchCategory> defaultCat = categoryRepository.findByName("General");
        if (defaultCat.isPresent()) {
            return defaultCat.get();
        }
        
        ResearchCategory general = new ResearchCategory("General", "General Research Interests");
        return categoryRepository.save(general);
    }
    
    public void initializeDefaultData() {
        if (categoryRepository.count() == 0) {
            // Create default categories and interests
            createDefaultCategories();
        }
    }
    
    private void createDefaultCategories() {
        // Engineering
        ResearchCategory engineering = new ResearchCategory("Engineering", "Engineering and Technology Fields");
        engineering = categoryRepository.save(engineering);
        
        interestRepository.save(new ResearchInterest("Civil Engineering", "Infrastructure, Construction, and Environmental Engineering", engineering));
        interestRepository.save(new ResearchInterest("Computer Engineering", "Hardware and Software Systems", engineering));
        interestRepository.save(new ResearchInterest("Electrical Engineering", "Power Systems and Electronics", engineering));
        interestRepository.save(new ResearchInterest("Mechanical Engineering", "Machines, Energy, and Manufacturing", engineering));
        
        // Computer Science
        ResearchCategory computerScience = new ResearchCategory("Computer Science", "Computing and Information Technology");
        computerScience = categoryRepository.save(computerScience);
        
        interestRepository.save(new ResearchInterest("Artificial Intelligence", "Machine Learning and AI Systems", computerScience));
        interestRepository.save(new ResearchInterest("Software Engineering", "Software Development and Architecture", computerScience));
        interestRepository.save(new ResearchInterest("Data Science", "Big Data and Analytics", computerScience));
        interestRepository.save(new ResearchInterest("Cybersecurity", "Information Security and Privacy", computerScience));
        
        // Business
        ResearchCategory business = new ResearchCategory("Business", "Business Administration and Management");
        business = categoryRepository.save(business);
        
        interestRepository.save(new ResearchInterest("Marketing", "Digital Marketing and Consumer Behavior", business));
        interestRepository.save(new ResearchInterest("Finance", "Financial Analysis and Investment", business));
        interestRepository.save(new ResearchInterest("Management", "Organizational Behavior and Leadership", business));
        
        // Sciences
        ResearchCategory sciences = new ResearchCategory("Sciences", "Natural and Physical Sciences");
        sciences = categoryRepository.save(sciences);
        
        interestRepository.save(new ResearchInterest("Biology", "Life Sciences and Biotechnology", sciences));
        interestRepository.save(new ResearchInterest("Chemistry", "Chemical Research and Materials", sciences));
        interestRepository.save(new ResearchInterest("Physics", "Theoretical and Applied Physics", sciences));
        interestRepository.save(new ResearchInterest("Mathematics", "Applied and Pure Mathematics", sciences));
    }
}
