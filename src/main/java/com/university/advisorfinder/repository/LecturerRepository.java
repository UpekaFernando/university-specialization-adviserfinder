package com.university.advisorfinder.repository;

import com.university.advisorfinder.model.Lecturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface LecturerRepository extends JpaRepository<Lecturer, Long> {
    
    Optional<Lecturer> findByEmail(String email);
    
    List<Lecturer> findByDepartmentContainingIgnoreCase(String department);
    
    @Query("SELECT DISTINCT l FROM Lecturer l JOIN l.researchInterests ri WHERE ri.id IN :interestIds")
    List<Lecturer> findByResearchInterestIds(@Param("interestIds") List<Long> interestIds);
    
    @Query("SELECT DISTINCT l FROM Lecturer l JOIN l.researchInterests ri WHERE ri.name LIKE %:keyword% OR l.firstName LIKE %:keyword% OR l.lastName LIKE %:keyword% OR l.department LIKE %:keyword%")
    List<Lecturer> searchByKeyword(@Param("keyword") String keyword);
    
    @Query("SELECT DISTINCT l FROM Lecturer l JOIN l.researchInterests ri WHERE ri.category.id = :categoryId")
    List<Lecturer> findByResearchCategoryId(@Param("categoryId") Long categoryId);
    
    @Query("SELECT DISTINCT l FROM Lecturer l JOIN l.researchInterests ri WHERE ri.name IN :interestNames")
    List<Lecturer> findByResearchInterests_NameIn(@Param("interestNames") List<String> interestNames);
}
