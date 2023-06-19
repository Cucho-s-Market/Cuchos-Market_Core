package com.project.cuchosmarket.repositories;

import com.project.cuchosmarket.dto.DtOrder;
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

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    @Query("SELECT DISTINCT new com.project.cuchosmarket.dto.DtOrder(" +
            "o.id, " +
            "o.totalPrice, " +
            "o.creationDate, " +
            "o.endDate, " +
            "o.status, " +
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


}
