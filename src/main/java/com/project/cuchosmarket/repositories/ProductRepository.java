package com.project.cuchosmarket.repositories;

import com.project.cuchosmarket.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductRepository extends JpaRepository<Product, String>, JpaSpecificationExecutor<Product> {
    Boolean existsByName(String name);
    Product findByCode(String code);
}
