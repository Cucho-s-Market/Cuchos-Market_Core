package com.project.cuchosmarket.repositories;

import com.project.cuchosmarket.dto.DtProductStock;
import com.project.cuchosmarket.models.Stock;
import com.project.cuchosmarket.models.StockId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockRepository extends JpaRepository<Stock, StockId> {
    @Query("SELECT new com.project.cuchosmarket.dto.DtProductStock(s.id.product.code AS code, " +
            "s.id.product.name AS name, " +
            "s.id.product.description AS description, " +
            "s.id.product.price AS price, " +
            "s.id.product.entryDate AS entryDate, " +
            "s.id.product.brand AS brand, " +
            "s.id.product.category.id AS categoryId, " +
            "s.id.product.images AS images, " +
            "s.quantity AS quantity) " +
            "FROM Stock s " +
            "WHERE s.id.product.name = :productId AND s.id.branch.id = :branchId")
    DtProductStock findProductAndQuantityByBranch(@Param("productId") String productId,
                                                  @Param("branchId") Long branchId);

    @Query("SELECT new com.project.cuchosmarket.dto.DtProductStock(s.id.product.code AS code, " +
            "s.id.product.name AS name, " +
            "s.id.product.description AS description, " +
            "s.id.product.price AS price, " +
            "s.id.product.entryDate AS entryDate, " +
            "s.id.product.brand AS brand, " +
            "s.id.product.category.id AS categoryId, " +
            "s.id.product.images AS images, " +
            "s.quantity AS quantity) " +
            "FROM Stock s " +
            "WHERE s.id.branch.id = :branchId " +
            "AND s.id.product.category.id = :categoryId")
    List<DtProductStock> findProductsAndQuantitiesByBranchAndCategory(@Param("branchId") Long branchId,
                                                                      @Param("categoryId") Long categoryId);
}