package com.university.advisorfinder.service;

import com.university.advisorfinder.dto.StudentRegistrationDTO;
import com.university.advisorfinder.model.Student;
import com.university.advisorfinder.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    @Test
    @DisplayName("Should reject invalid email format")
    void registerStudent_invalidEmail_throwsException() {
        // Arrange
        StudentRegistrationDTO dto = new StudentRegistrationDTO();
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setEmail("invalid-email"); // Invalid email format

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> studentService.registerStudent(dto));
        
        assertTrue(exception.getMessage().toLowerCase().contains("email"));
        verify(studentRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should reject duplicate email")
    void registerStudent_duplicateEmail_throwsException() {
        // Arrange
        StudentRegistrationDTO dto = new StudentRegistrationDTO();
        dto.setFirstName("Jane");
        dto.setLastName("Smith");
        dto.setEmail("jane@example.com");
        
        when(studentRepository.existsByEmail("jane@example.com")).thenReturn(true);

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, 
            () -> studentService.registerStudent(dto));
        
        assertTrue(exception.getMessage().toLowerCase().contains("exists"));
        verify(studentRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should save student with valid email")
    void registerStudent_validEmail_success() {
        // Arrange
        StudentRegistrationDTO dto = new StudentRegistrationDTO();
        dto.setFirstName("Alice");
        dto.setLastName("Johnson");
        dto.setEmail("alice@example.com");
        
        when(studentRepository.existsByEmail("alice@example.com")).thenReturn(false);
        when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> {
            Student student = invocation.getArgument(0);
            student.setId(1L);
            return student;
        });

        // Act
        Student result = studentService.registerStudent(dto);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("alice@example.com", result.getEmail());
        verify(studentRepository).save(any(Student.class));
    }
}