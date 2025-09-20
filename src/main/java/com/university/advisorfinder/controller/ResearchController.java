package com.university.advisorfinder.controller;

import com.university.advisorfinder.model.ResearchCategory;
import com.university.advisorfinder.model.ResearchInterest;
import com.university.advisorfinder.service.ResearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/research")
@CrossOrigin(origins = "http://localhost:3000")
public class ResearchController {
    
    @Autowired
    private ResearchService researchService;
    
    @GetMapping("/categories")
    public ResponseEntity<List<ResearchCategory>> getAllCategories() {
        List<ResearchCategory> categories = researchService.getAllCategories();
        return ResponseEntity.ok(categories);
    }
    
    @GetMapping("/interests")
    public ResponseEntity<List<ResearchInterest>> getAllInterests() {
        List<ResearchInterest> interests = researchService.getAllInterests();
        return ResponseEntity.ok(interests);
    }
    
    @GetMapping("/interests/category/{categoryId}")
    public ResponseEntity<List<ResearchInterest>> getInterestsByCategory(@PathVariable Long categoryId) {
        List<ResearchInterest> interests = researchService.getInterestsByCategory(categoryId);
        return ResponseEntity.ok(interests);
    }
    
    @GetMapping("/interests/search")
    public ResponseEntity<List<ResearchInterest>> searchInterests(@RequestParam String keyword) {
        List<ResearchInterest> interests = researchService.searchInterestsByKeyword(keyword);
        return ResponseEntity.ok(interests);
    }
}
