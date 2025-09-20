package com.university.advisorfinder.controller;

import com.university.advisorfinder.dto.StudentRegistrationDTO;
import com.university.advisorfinder.model.Student;
import com.university.advisorfinder.service.StudentService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "http://localhost:3000")
public class StudentController {
    
    @Autowired
    private StudentService studentService;
    
    @PostMapping("/register")
    public ResponseEntity<?> registerStudent(@Valid @RequestBody StudentRegistrationDTO registrationDTO) {
        try {
            Student student = studentService.registerStudent(registrationDTO);
            // Return properly structured JSON response
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Registration successful");
            response.put("studentId", student.getId());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmailExists(@RequestParam String email) {
        boolean exists = studentService.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }
    
    @GetMapping("/profile")
    public ResponseEntity<Student> getStudentProfile(@RequestParam String email) {
        return studentService.findByEmail(email)
                .map(student -> ResponseEntity.ok(student))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/all")
    public ResponseEntity<?> getAllStudents() {
        try {
            return ResponseEntity.ok(studentService.getAllStudents());
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to retrieve students");
            return ResponseEntity.badRequest().body(error);
        }
    }
}
