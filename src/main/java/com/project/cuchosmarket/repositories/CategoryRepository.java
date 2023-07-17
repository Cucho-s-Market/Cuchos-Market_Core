package com.project.cuchosmarket.repositories;

import com.project.cuchosmarket.dto.DtCategory;
import com.project.cuchosmarket.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);
    Boolean existsByName(String name);




}
