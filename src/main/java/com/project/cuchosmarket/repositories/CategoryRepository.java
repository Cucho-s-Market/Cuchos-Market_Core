package com.project.cuchosmarket.repositories;

import com.project.cuchosmarket.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("select c from Category c order by c.categoryParent")
    List<Category> findByOrderByCategoryParentAsc();
    Category findByName(String name);
    Boolean existsByName(String name);
}
