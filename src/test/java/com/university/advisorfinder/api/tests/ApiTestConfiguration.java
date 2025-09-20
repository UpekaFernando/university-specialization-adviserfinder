package com.university.advisorfinder.api.tests;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/**
 * Test Configuration to override default data initialization
 * Prevents CommandLineRunner from running during API tests
 */
@TestConfiguration
public class ApiTestConfiguration {
    
    /**
     * Mock CommandLineRunner that does nothing during tests
     * This prevents the default data initialization from running
     */
    @Bean
    @Primary
    public CommandLineRunner mockDataInitializer() {
        return args -> {
            // Do nothing - prevent data initialization during tests
        };
    }
}