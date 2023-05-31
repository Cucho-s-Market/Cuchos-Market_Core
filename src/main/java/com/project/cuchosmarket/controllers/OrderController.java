package com.project.cuchosmarket.controllers;

import com.project.cuchosmarket.dto.DtOrder;
import com.project.cuchosmarket.dto.DtResponse;
import com.project.cuchosmarket.exceptions.EmployeeNotWorksInException;
import com.project.cuchosmarket.exceptions.OrderNotExistException;
import com.project.cuchosmarket.exceptions.*;
import com.project.cuchosmarket.models.Order;
import com.project.cuchosmarket.security.JwtService;
import com.project.cuchosmarket.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final JwtService jwtService;

    @GetMapping("/{branch_id}")
    public DtResponse getOrdersHistory(@PathVariable("branch_id") Long branch_id,
                                       @RequestParam(value = "orderStatus", required = false) String orderStatus,
                                       @RequestParam(value = "startDate", required = false) LocalDate startDate,
                                       @RequestParam(value = "endDate", required = false) LocalDate endDate,
                                       @RequestParam(value = "orderDirection", required = false) String orderDirection) {
        List<Order> orderList = null;
        try {
            String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            orderList = orderService.getOrdersBy(userEmail, branch_id, orderStatus,
                    startDate, endDate, orderDirection);
        } catch (EmployeeNotWorksInException | IllegalArgumentException | UserNotExistException e) {
            return DtResponse.builder()
                    .error(true)
                    .message(e.getMessage())
                    .build();
        }
        return DtResponse.builder()
                .error(false)
                .message(String.valueOf(orderList.size()))
                .data(orderList)
                .build();
    }

    @PostMapping
    public DtResponse purchaseProducts(@RequestBody DtOrder order) {
        try {
            String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            orderService.buyProducts(userEmail, order);
        } catch (BranchNotExistException | ProductNotExistException | UserNotExistException | NoStockException e) {
            return DtResponse.builder()
                    .error(true)
                    .message(e.getMessage())
                    .build();
        }

        return DtResponse.builder()
                .error(false)
                .message("Compra realizada con exito")
                .build();
    }

    @PutMapping("/update-status")
    public DtResponse updateOrderStatus(@RequestHeader("Authorization") String authorizationHeader, @RequestBody DtOrder order) {
        String userEmail = jwtService.extractUsername(authorizationHeader.substring(7));
        try {
            orderService.updateStatus(userEmail, order);
        } catch (EmployeeNotWorksInException | OrderNotExistException e) {
            return DtResponse.builder()
                    .error(true)
                    .message(e.getMessage())
                    .build();
        }

        return DtResponse.builder()
                .error(false)
                .message("Estado de orden " + order.getId() + " actualizada: " + order.getStatus().name()).build();
    }
}
