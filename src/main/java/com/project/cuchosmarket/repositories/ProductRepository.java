package com.project.cuchosmarket.repositories;

import com.project.cuchosmarket.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {
    Boolean existsByName(String name);
}
