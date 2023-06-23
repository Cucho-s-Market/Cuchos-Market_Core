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
            "CASE WHEN (:includeExpiredPromotions = true OR prom.endDate >= CURRENT_DATE) AND (prom.percentage IS NOT NULL) THEN (p.price * (1 - (prom.percentage / 100.0))) ELSE p.price END AS finalPrice, " +
            "p.entryDate AS entryDate, " +
            "p.brand AS brand, " +
            "p.category.id AS categoryId, " +
            "p.images AS images, " +
            "CASE WHEN (:includeExpiredPromotions = true OR prom.endDate >= CURRENT_DATE) AND (TYPE(prom) = com.project.cuchosmarket.models.NxM) THEN true ELSE false END AS nxmPromotion, " +
            "CASE WHEN (:includeExpiredPromotions = true OR prom.endDate >= CURRENT_DATE) AND (TYPE(prom) = com.project.cuchosmarket.models.NxM) THEN prom.n ELSE NULL END AS n, " +
            "CASE WHEN (:includeExpiredPromotions = true OR prom.endDate >= CURRENT_DATE) AND (TYPE(prom) = com.project.cuchosmarket.models.NxM) THEN prom.m ELSE NULL END AS m) " +
            "FROM Product p " +
            "LEFT JOIN p.promotions prom " +
            "WHERE (:code IS NULL OR p.code = :code) " +
            "AND (:name IS NULL OR LOWER(p.name) LIKE CONCAT('%', LOWER(:name), '%')) " +
            "AND (:brand IS NULL OR LOWER(p.brand) = LOWER(:brand)) " +
            "AND (:categoryId IS NULL OR p.category.id = :categoryId) " +
            "AND (:promotionId IS NULL OR (prom.id = :promotionId AND (:includeExpiredPromotions = true OR prom.endDate >= CURRENT_DATE)))")
    Page<DtProduct> findProducts(@Param("code") String code,
                                 @Param("name") String name,
                                 @Param("brand") String brand,
                                 @Param("categoryId") Long categoryId,
                                 @Param("promotionId") Long promotionId,
                                 @Param("includeExpiredPromotions") boolean includeExpiredPromotions,
                                 Pageable pageable);

}
