package com.university.advisorfinder.config;

import com.university.advisorfinder.service.LecturerService;
import com.university.advisorfinder.service.ResearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private ResearchService researchService;
    
    @Autowired
    private LecturerService lecturerService;
    
    @Override
    public void run(String... args) throws Exception {
        researchService.initializeDefaultData();
        lecturerService.initializeSampleLecturers();
    }
}
