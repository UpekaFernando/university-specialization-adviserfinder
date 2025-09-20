package com.university.advisorfinder.service;

import com.university.advisorfinder.dto.StudentRegistrationDTO;
import com.university.advisorfinder.model.Student;
import com.university.advisorfinder.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class StudentService {
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    /**
     * Registers a new student with comprehensive validation.
     * TDD REFACTOR PHASE: Improved code organization and readability.
     * 
     * @param registrationDTO the student registration data
     * @return the registered Student entity
     * @throws IllegalArgumentException if email format is invalid
     * @throws IllegalStateException if email already exists
     */
    public Student registerStudent(StudentRegistrationDTO registrationDTO) {
        // Enhanced validation using extracted methods
        validateEmailFormat(registrationDTO.getEmail());
        checkForDuplicateEmail(registrationDTO.getEmail());
        
        // Create and save the student
        return createAndSaveStudent(registrationDTO);
    }
    
    /**
     * Validates email format with improved logic.
     * @param email the email to validate
     * @throws IllegalArgumentException if email format is invalid
     */
    private void validateEmailFormat(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        
        // Enhanced email validation (still minimal but more robust)
        if (!email.contains("@") || email.indexOf("@") == 0 || email.indexOf("@") == email.length() - 1) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }
    
    /**
     * Checks if email already exists in the database.
     * @param email the email to check
     * @throws IllegalStateException if email already exists
     */
    private void checkForDuplicateEmail(String email) {
        if (studentRepository.existsByEmail(email)) {
            throw new IllegalStateException("Email already exists");
        }
    }
    
    /**
     * Creates and saves a new Student entity.
     * @param registrationDTO the registration data
     * @return the saved Student entity
     */
    private Student createAndSaveStudent(StudentRegistrationDTO registrationDTO) {
        // Handle potential student ID conflicts
        if (registrationDTO.getStudentId() != null && 
            studentRepository.existsByStudentId(registrationDTO.getStudentId())) {
            throw new RuntimeException("Student ID already exists");
        }
        Student student = new Student();
        student.setFirstName(registrationDTO.getFirstName());
        student.setLastName(registrationDTO.getLastName());
        student.setEmail(registrationDTO.getEmail());
        student.setPhone(registrationDTO.getPhone());
        student.setStudentId(registrationDTO.getStudentId());
        student.setProgram(registrationDTO.getProgram());
        student.setYearOfStudy(registrationDTO.getYearOfStudy());
        student.setInterests(registrationDTO.getInterests());
        
        // Hash the password before saving
        if (registrationDTO.getPassword() != null) {
            String hashedPassword = passwordEncoder.encode(registrationDTO.getPassword());
            student.setPassword(hashedPassword);
        }
        
        return studentRepository.save(student);
    }
    
    public Optional<Student> findByEmail(String email) {
        return studentRepository.findByEmail(email);
    }
    
    public Optional<Student> findById(Long id) {
        return studentRepository.findById(id);
    }
    
    public boolean existsByEmail(String email) {
        return studentRepository.existsByEmail(email);
    }
    
    /**
     * Retrieves all students from the database.
     * @return a list of all students
     */
    public java.util.List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
}
