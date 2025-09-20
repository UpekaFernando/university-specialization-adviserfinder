package com.university.advisorfinder.controller;

import com.university.advisorfinder.dto.LecturerContactDTO;
import com.university.advisorfinder.dto.LecturerPublicDTO;
import com.university.advisorfinder.model.Lecturer;
import com.university.advisorfinder.service.LecturerService;
import com.university.advisorfinder.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/lecturers")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
public class LecturerController {
    
    @Autowired
    private LecturerService lecturerService;
    
    @Autowired
    private StudentService studentService;
    
    @GetMapping(value = "/public")
    public ResponseEntity<List<LecturerPublicDTO>> getAllLecturersPublic() {
        List<LecturerPublicDTO> lecturers = lecturerService.getAllLecturersPublic();
        return ResponseEntity.ok(lecturers);
    }
    
    @GetMapping(value = "/search")
    public ResponseEntity<List<LecturerPublicDTO>> searchLecturers(@RequestParam String keyword) {
        List<LecturerPublicDTO> lecturers = lecturerService.searchLecturersByKeyword(keyword);
        return ResponseEntity.ok(lecturers);
    }
    
    @GetMapping(value = "/by-interests")
    public ResponseEntity<List<LecturerPublicDTO>> findLecturersByInterests(@RequestParam List<Long> interestIds) {
        List<LecturerPublicDTO> lecturers = lecturerService.findLecturersByResearchInterests(interestIds);
        return ResponseEntity.ok(lecturers);
    }
    
    @GetMapping(value = "/by-category/{categoryId}")
    public ResponseEntity<List<LecturerPublicDTO>> findLecturersByCategory(@PathVariable Long categoryId) {
        List<LecturerPublicDTO> lecturers = lecturerService.findLecturersByCategory(categoryId);
        return ResponseEntity.ok(lecturers);
    }
    
    @GetMapping(value = "/by-department")
    public ResponseEntity<List<LecturerPublicDTO>> findLecturersByDepartment(@RequestParam String department) {
        List<LecturerPublicDTO> lecturers = lecturerService.findLecturersByDepartment(department);
        return ResponseEntity.ok(lecturers);
    }
    
    @GetMapping("/{id}/contact")
    public ResponseEntity<LecturerContactDTO> getLecturerContact(
            @PathVariable Long id, 
            @RequestParam String studentEmail) {
        
        // Verify student exists to access contact information
        if (!studentService.existsByEmail(studentEmail)) {
            return ResponseEntity.status(403).build(); // Forbidden - student not registered
        }
        
        return lecturerService.getLecturerContact(id)
                .map(contact -> ResponseEntity.ok(contact))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/register")
    public ResponseEntity<?> registerLecturer(@RequestBody Lecturer lecturer) {
        Lecturer saved = lecturerService.saveLecturer(lecturer);
        return ResponseEntity.ok(saved);
    }
}
