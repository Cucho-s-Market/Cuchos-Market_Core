package com.project.cuchosmarket.repositories;

import com.project.cuchosmarket.dto.DtProduct;
import com.project.cuchosmarket.models.Stock;
import com.project.cuchosmarket.models.StockId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Stock, StockId> {

    @Query("SELECT DISTINCT new com.project.cuchosmarket.dto.DtProduct(" +
            "s.id.product.code AS code, " +
            "s.id.product.name AS name, " +
            "s.id.product.description AS description, " +
            "s.id.product.price AS price, " +
            "CASE WHEN (:includeExpiredPromotions = true OR p.endDate >= CURRENT_DATE) AND (p.percentage IS NOT NULL) THEN (s.id.product.price * (1 - (p.percentage / 100.0))) ELSE s.id.product.price END AS finalPrice, " +
            "s.id.product.entryDate AS entryDate, " +
            "s.id.product.brand AS brand, " +
            "s.id.product.category.id AS categoryId, " +
            "s.id.product.images AS images, " +
            "s.quantity AS quantity, " +
            "CASE WHEN (:includeExpiredPromotions = true OR p.endDate >= CURRENT_DATE) AND (TYPE(p) = com.project.cuchosmarket.models.NxM) THEN true ELSE false END AS nxmPromotion, " +
            "CASE WHEN (:includeExpiredPromotions = true OR p.endDate >= CURRENT_DATE) AND (TYPE(p) = com.project.cuchosmarket.models.NxM) THEN p.n ELSE NULL END AS n, " +
            "CASE WHEN (:includeExpiredPromotions = true OR p.endDate >= CURRENT_DATE) AND (TYPE(p) = com.project.cuchosmarket.models.NxM) THEN p.m ELSE NULL END AS m) " +
            "FROM Stock s " +
            "LEFT JOIN s.id.product.promotions p " +
            "WHERE (:branchId IS NULL OR s.id.branch.id = :branchId) " +
            "AND (:code IS NULL OR s.id.product.code = :code) " +
            "AND (:name IS NULL OR LOWER(s.id.product.name) LIKE CONCAT('%', LOWER(:name), '%')) " +
            "AND (:brand IS NULL OR LOWER(s.id.product.brand) = LOWER(:brand)) " +
            "AND (:categoryId IS NULL OR s.id.product.category.id = :categoryId) " +
            "AND (:promotionId IS NULL OR (p.id = :promotionId AND (:includeExpiredPromotions = true OR p.endDate >= CURRENT_DATE)))")
    Page<DtProduct> findProducts(@Param("branchId") Long branchId,
                                 @Param("code") String code,
                                 @Param("name") String name,
                                 @Param("brand") String brand,
                                 @Param("categoryId") Long categoryId,
                                 @Param("promotionId") Long promotionId,
                                 @Param("includeExpiredPromotions") boolean includeExpiredPromotions,
                                 Pageable pageable);



}