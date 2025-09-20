package com.university.advisorfinder.repository;

import com.university.advisorfinder.model.ResearchCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ResearchCategoryRepository extends JpaRepository<ResearchCategory, Long> {
    
    Optional<ResearchCategory> findByName(String name);
    
    boolean existsByName(String name);
}
