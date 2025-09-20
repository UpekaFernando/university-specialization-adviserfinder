package com.university.advisorfinder.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/university")
@CrossOrigin(origins = "*")
public class UniversityTestController {
    
    @GetMapping("/students")
    public ResponseEntity<?> getAllStudents() {
        List<Map<String, Object>> students = new ArrayList<>();
        
        Map<String, Object> student1 = new HashMap<>();
        student1.put("id", 1);
        student1.put("firstName", "Alice");
        student1.put("lastName", "Johnson");
        student1.put("email", "alice.johnson@university.edu");
        student1.put("program", "Computer Science");
        student1.put("year", 3);
        student1.put("gpa", 3.85);
        
        Map<String, Object> student2 = new HashMap<>();
        student2.put("id", 2);
        student2.put("firstName", "Bob");
        student2.put("lastName", "Smith");
        student2.put("email", "bob.smith@university.edu");
        student2.put("program", "Engineering");
        student2.put("year", 2);
        student2.put("gpa", 3.92);
        
        students.add(student1);
        students.add(student2);
        
        Map<String, Object> response = new HashMap<>();
        response.put("students", students);
        response.put("total", students.size());
        response.put("status", "success");
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/lecturers")
    public ResponseEntity<?> getAllLecturers() {
        List<Map<String, Object>> lecturers = new ArrayList<>();
        
        Map<String, Object> lecturer1 = new HashMap<>();
        lecturer1.put("id", 1);
        lecturer1.put("name", "Dr. Sarah Wilson");
        lecturer1.put("department", "Computer Science");
        lecturer1.put("specialization", "Artificial Intelligence");
        lecturer1.put("email", "sarah.wilson@university.edu");
        
        Map<String, Object> lecturer2 = new HashMap<>();
        lecturer2.put("id", 2);
        lecturer2.put("name", "Prof. Michael Brown");
        lecturer2.put("department", "Engineering");
        lecturer2.put("specialization", "Robotics");
        lecturer2.put("email", "michael.brown@university.edu");
        
        lecturers.add(lecturer1);
        lecturers.add(lecturer2);
        
        Map<String, Object> response = new HashMap<>();
        response.put("lecturers", lecturers);
        response.put("total", lecturers.size());
        response.put("status", "success");
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/students/search")
    public ResponseEntity<?> searchStudents(@RequestBody(required = false) Map<String, Object> searchData) {
        List<Map<String, Object>> students = new ArrayList<>();
        
        Map<String, Object> student = new HashMap<>();
        student.put("id", 1);
        student.put("firstName", "Alice");
        student.put("lastName", "Johnson");
        student.put("email", "alice.johnson@university.edu");
        student.put("program", "Computer Science");
        student.put("year", 3);
        student.put("gpa", 3.85);
        student.put("searchTerm", searchData != null ? searchData.get("program") : "Computer Science");
        
        students.add(student);
        
        Map<String, Object> response = new HashMap<>();
        response.put("results", students);
        response.put("searchCriteria", searchData);
        response.put("total", students.size());
        response.put("status", "success");
        
        return ResponseEntity.ok(response);
    }
}