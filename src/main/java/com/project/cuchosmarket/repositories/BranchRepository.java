package com.project.cuchosmarket.repositories;

import com.project.cuchosmarket.dto.DtBranch;
import com.project.cuchosmarket.dto.DtIssue;
import com.project.cuchosmarket.dto.DtOrder;
import com.project.cuchosmarket.dto.DtStatistics;
import com.project.cuchosmarket.enums.OrderStatus;
import com.project.cuchosmarket.models.Branch;
import com.project.cuchosmarket.models.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {
    @Query("""
    SELECT new com.project.cuchosmarket.dto.DtBranch(
        b.id,
        b.name,
        b.address
    )
    FROM Branch b
    """)
    List<DtBranch> findAllSelected();

    @Query("SELECT DISTINCT new com.project.cuchosmarket.dto.DtOrder(" +
            "b.id, " +
            "o.id, " +
            "o.totalPrice, " +
            "o.creationDate, " +
            "o.endDate, " +
            "o.status, " +
            "o.type) " +
            "FROM Branch b " +
            "JOIN b.orders o " +
            "WHERE b.id = :branchId " +
            "AND (:orderStatus IS NULL OR o.status = :orderStatus) " +
            "AND (:startDate IS NULL OR o.creationDate >= :startDate) " +
            "AND (:endDate IS NULL OR o.creationDate <= :endDate)")
    Page<DtOrder> findOrders(@Param("branchId") Long branchId,
                             @Param("orderStatus") OrderStatus orderStatus,
                             @Param("startDate") LocalDate startDate,
                             @Param("endDate") LocalDate endDate,
                             Pageable pageable);

    Branch findByOrdersContains(Order order);

    @Query("SELECT DISTINCT new com.project.cuchosmarket.dto.DtIssue(" +
            "i.title, " +
            "i.description, " +
            "i.creationDate, " +
            "i.userEmail, " +
            "i.order.id) " +
            "FROM Branch b " +
            "JOIN b.issues i " +
            "WHERE b.id = :branchId ")
    Page<DtIssue> findIssues(@Param("branchId") Long branchId,
                             Pageable pageable);

    @Query("SELECT DISTINCT new com.project.cuchosmarket.dto.DtStatistics$DtTopProduct(" +
            "i.name AS productName, SUM(i.quantity) AS salesCount) " +
            "FROM Branch b " +
            "JOIN b.orders o JOIN o.products i " +
            "WHERE b.id = :branchId " +
            "AND o.creationDate >= :startDate AND o.creationDate <= :endDate " +
            "AND o.status = 'DELIVERED' " +
            "GROUP BY i.name " +
            "ORDER BY SUM(i.quantity) DESC " +
            "LIMIT 10")
    List<DtStatistics.DtTopProduct> findTopSellingProductsByBranch(@Param("branchId") Long branchId,
                                                                   @Param("startDate") LocalDate startDate,
                                                                   @Param("endDate") LocalDate endDate);

    @Query("SELECT DISTINCT new com.project.cuchosmarket.dto.DtStatistics$DtSalesByBranch(" +
            "b.name AS branchName, COUNT(o) AS totalSales) " +
            "FROM Branch b " +
            "JOIN b.orders o " +
            "WHERE o.creationDate >= :startDate AND o.creationDate <= :endDate " +
            "AND o.status = 'DELIVERED' " +
            "GROUP BY b.name " +
            "ORDER BY COUNT(o) DESC")
    List<DtStatistics.DtSalesByBranch> findSales(@Param("startDate") LocalDate startDate,
                                                 @Param("endDate") LocalDate endDate);

    @Query("SELECT new com.project.cuchosmarket.dto.DtStatistics$DtSalesInBranch(" +
            "COUNT(o), " +
            "SUM(CASE WHEN o.status = 'DELIVERED' THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN o.status = 'CANCELLED' THEN 1 ELSE 0 END)) " +
            "FROM Branch b " +
            "JOIN b.orders o " +
            "WHERE b.id = :branchId " +
            "AND o.creationDate >= :startDate AND o.creationDate <= :endDate")
    List<DtStatistics.DtSalesInBranch> findSalesInBranch(@Param("branchId") Long branchId,
                                                         @Param("startDate") LocalDate startDate,
                                                         @Param("endDate") LocalDate endDate);
}
