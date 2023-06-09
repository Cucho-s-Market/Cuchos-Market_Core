package com.project.cuchosmarket.repositories;

import com.project.cuchosmarket.models.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PromotionRepository extends JpaRepository<Promotion, Long> {
    @Query("SELECT p FROM Promotion p WHERE (:includeExpired = true OR p.endDate >= CURRENT_DATE)")
    List<Promotion> findPromotions(@Param("includeExpired") boolean includeExpired);
}
