package com.project.cuchosmarket.repositories;

import com.project.cuchosmarket.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
