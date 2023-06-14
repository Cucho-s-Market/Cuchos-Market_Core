package com.project.cuchosmarket.repositories;

import com.project.cuchosmarket.dto.DtProduct;
import com.project.cuchosmarket.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, String>, JpaSpecificationExecutor<Product> {
    Boolean existsByName(String name);
    Product findByCode(String code);

    @Query("SELECT DISTINCT new com.project.cuchosmarket.dto.DtProduct(" +
            "p.code AS code, " +
            "p.name AS name, " +
            "p.description AS description, " +
            "p.price AS price, " +
            "p.entryDate AS entryDate, " +
            "p.brand AS brand, " +
            "p.category.id AS categoryId, " +
            "p.images AS images) " +
            "FROM Product p " +
            "WHERE (:code IS NULL OR p.code = :code) " +
            "AND (:name IS NULL OR LOWER(p.name) LIKE CONCAT('%', LOWER(:name), '%')) " +
            "AND (:brand IS NULL OR LOWER(p.brand) = LOWER(:brand)) " +
            "AND (:categoryId IS NULL OR p.category.id = :categoryId)")
    Page<DtProduct> findProducts(@Param("code") String code,
                                 @Param("name") String name,
                                 @Param("brand") String brand,
                                 @Param("categoryId") Long categoryId,
                                 Pageable pageable);
}
