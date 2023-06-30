package com.project.cuchosmarket.repositories;

import com.project.cuchosmarket.dto.DtOrder;
import com.project.cuchosmarket.dto.DtStatistics;
import com.project.cuchosmarket.enums.OrderStatus;
import com.project.cuchosmarket.models.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    @Query("SELECT DISTINCT new com.project.cuchosmarket.dto.DtOrder(" +
            "o.id, " +
            "o.totalPrice, " +
            "o.creationDate, " +
            "o.endDate, " +
            "o.status, " +
            "o.clientAddress, " +
            "o.type) " +
            "FROM Order o " +
            "JOIN o.customer c " +
            "WHERE c.id = :customerId " +
            "AND (:orderStatus IS NULL OR o.status = :orderStatus) " +
            "AND (:startDate IS NULL OR o.creationDate >= :startDate) " +
            "AND (:endDate IS NULL OR o.creationDate <= :endDate)")
    Page<DtOrder> findCustomerOrders(@Param("customerId") Long customerId,
                                     @Param("orderStatus") OrderStatus orderStatus,
                                     @Param("startDate") LocalDate startDate,
                                     @Param("endDate") LocalDate endDate,
                                     Pageable pageable);


    @Query("SELECT DISTINCT new com.project.cuchosmarket.dto.DtStatistics$DtTopProduct(" +
            "i.name AS productName, SUM(i.quantity) AS salesCount) " +
            "FROM Order o JOIN o.products i " +
            "WHERE o.creationDate >= :startDate AND o.creationDate <= :endDate " +
            "AND o.status = 'DELIVERED' " +
            "GROUP BY i.name " +
            "ORDER BY SUM(i.quantity) DESC " +
            "LIMIT 10")
    List<DtStatistics.DtTopProduct> findTopSellingProducts(@Param("startDate") LocalDate startDate,
                                                           @Param("endDate") LocalDate endDate);
    @Query("SELECT new com.project.cuchosmarket.dto.DtStatistics$DtPopularBrand(" +
            "p.brand AS brandName, COUNT(i) AS salesCount) " +
            "FROM Order o " +
            "JOIN o.products i JOIN i.product p " +
            "WHERE o.creationDate >= :startDate AND o.creationDate <= :endDate " +
            "AND o.status = 'DELIVERED' " +
            "GROUP BY p.brand " +
            "ORDER BY COUNT(i) DESC " +
            "LIMIT 10")
    List<DtStatistics.DtPopularBrand> findTopBrands(@Param("startDate") LocalDate startDate,
                                                    @Param("endDate") LocalDate endDate);

    @Query("SELECT new com.project.cuchosmarket.dto.DtStatistics$DtSuccessfulPromotion(" +
            "prom.name AS promotionName, SUM(i.quantity) AS salesCount) " +
            "FROM Order o " +
            "JOIN o.products i JOIN i.product p " +
            "JOIN p.promotions prom " +
            "WHERE o.creationDate >= :startDate AND o.creationDate <= :endDate " +
            "AND o.status = 'DELIVERED' AND prom.endDate >= CURRENT_DATE " +
            "GROUP BY prom.name " +
            "ORDER BY SUM(i.quantity) DESC " +
            "LIMIT 10")
    List<DtStatistics.DtSuccessfulPromotion> findTopPromotions(@Param("startDate") LocalDate startDate,
                                                               @Param("endDate") LocalDate endDate);
}
