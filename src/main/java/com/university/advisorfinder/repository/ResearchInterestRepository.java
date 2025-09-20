package com.university.advisorfinder.repository;

import com.university.advisorfinder.model.ResearchInterest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ResearchInterestRepository extends JpaRepository<ResearchInterest, Long> {
    
    List<ResearchInterest> findByCategoryId(Long categoryId);
    
    List<ResearchInterest> findByNameContainingIgnoreCase(String name);
    
    Optional<ResearchInterest> findByName(String name);
    
    Optional<ResearchInterest> findByNameAndCategoryId(String name, Long categoryId);
    
    @Query("SELECT ri FROM ResearchInterest ri WHERE ri.name LIKE %:keyword% OR ri.description LIKE %:keyword%")
    List<ResearchInterest> searchByKeyword(@Param("keyword") String keyword);
}
