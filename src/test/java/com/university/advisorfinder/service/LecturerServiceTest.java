package com.university.advisorfinder.service;

import com.university.advisorfinder.model.Lecturer;
import com.university.advisorfinder.model.ResearchCategory;
import com.university.advisorfinder.model.ResearchInterest;
import com.university.advisorfinder.repository.LecturerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LecturerServiceTest {

    @Mock
    private LecturerRepository lecturerRepository;

    @InjectMocks
    private LecturerService lecturerService;

    @Test
    @DisplayName("Should return lecturers matching research interests")
    void searchLecturersByInterests_withValidInterests_returnsMatches() {
        // Arrange
        List<String> interestNames = Arrays.asList("Machine Learning", "Data Science");
        
        // Create a mock category
        ResearchCategory category = new ResearchCategory("Computer Science", "CS Category");
        category.setId(1L);
        
        ResearchInterest ml = new ResearchInterest("Machine Learning", "ML Description", category);
        ml.setId(1L);
        
        ResearchInterest ds = new ResearchInterest("Data Science", "DS Description", category);
        ds.setId(2L);
        
        Lecturer lecturer1 = new Lecturer();
        lecturer1.setId(1L);
        lecturer1.setFirstName("Dr. John");
        lecturer1.setLastName("Smith");
        lecturer1.setResearchInterests(new HashSet<>(Arrays.asList(ml)));
        
        Lecturer lecturer2 = new Lecturer();
        lecturer2.setId(2L);
        lecturer2.setFirstName("Dr. Jane");
        lecturer2.setLastName("Doe");
        lecturer2.setResearchInterests(new HashSet<>(Arrays.asList(ds, ml)));
        
        when(lecturerRepository.findByResearchInterests_NameIn(interestNames))
            .thenReturn(Arrays.asList(lecturer1, lecturer2));

        // Act
        List<Lecturer> result = lecturerService.searchLecturersByInterests(interestNames);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(lecturer1));
        assertTrue(result.contains(lecturer2));
        verify(lecturerRepository).findByResearchInterests_NameIn(interestNames);
    }

    @Test
    @DisplayName("Should return empty list for empty interests")
    void searchLecturersByInterests_withEmptyInterests_returnsEmpty() {
        // Arrange
        List<String> emptyInterests = Collections.emptyList();

        // Act
        List<Lecturer> result = lecturerService.searchLecturersByInterests(emptyInterests);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verifyNoInteractions(lecturerRepository);
    }

    @Test
    @DisplayName("Should return empty list when no lecturers match")
    void searchLecturersByInterests_noMatches_returnsEmpty() {
        // Arrange
        List<String> interestNames = Arrays.asList("Quantum Computing");
        when(lecturerRepository.findByResearchInterests_NameIn(interestNames))
            .thenReturn(Collections.emptyList());

        // Act
        List<Lecturer> result = lecturerService.searchLecturersByInterests(interestNames);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(lecturerRepository).findByResearchInterests_NameIn(interestNames);
    }
}