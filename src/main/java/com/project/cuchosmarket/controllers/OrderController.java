package com.project.cuchosmarket.controllers;

import com.project.cuchosmarket.dto.DtIssue;
import com.project.cuchosmarket.dto.DtOrder;
import com.project.cuchosmarket.dto.DtResponse;
import com.project.cuchosmarket.exceptions.*;
import com.project.cuchosmarket.services.IssueService;
import com.project.cuchosmarket.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final IssueService issueService;

    @GetMapping("/branch/{branch_id}")
    public DtResponse getOrdersHistory(@PathVariable("branch_id") Long branch_id,
                                       @RequestParam(value = "page_number", required = false, defaultValue = "0") int page_number,
                                       @RequestParam(value = "page_size", required = false, defaultValue = "50") int page_size,
                                       @RequestParam(value = "orderStatus", required = false) String orderStatus,
                                       @RequestParam(value = "startDate", required = false) LocalDate startDate,
                                       @RequestParam(value = "endDate", required = false) LocalDate endDate,
                                       @RequestParam(value = "orderDirection", required = false) String orderDirection) {
        Page<DtOrder> orderList = null;
        try {
            String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            orderList = orderService.getOrdersBy(userEmail, page_number, page_size, branch_id, orderStatus,
                    startDate, endDate, orderDirection);
        } catch (EmployeeNotWorksInException | UserNotExistException | InvalidOrderException e) {
            return DtResponse.builder()
                    .error(true)
                    .message(e.getMessage())
                    .build();
        }
        return DtResponse.builder()
                .error(false)
                .data(orderList)
                .build();
    }

    @GetMapping("/customer")
    public DtResponse getPurchasesHistory(@RequestParam(value = "page_number", required = false, defaultValue = "0") int page_number,
                                       @RequestParam(value = "page_size", required = false, defaultValue = "50") int page_size,
                                       @RequestParam(value = "orderStatus", required = false) String orderStatus,
                                       @RequestParam(value = "startDate", required = false) LocalDate startDate,
                                       @RequestParam(value = "endDate", required = false) LocalDate endDate,
                                       @RequestParam(value = "orderDirection", required = false) String orderDirection) {
        Page<DtOrder> orderList = null;
        try {
            String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            orderList = orderService.getPurchasesByCustomer(userEmail, page_number, page_size, orderStatus,
                    startDate, endDate, orderDirection);
        } catch (UserNotExistException | InvalidOrderException e) {
            return DtResponse.builder()
                    .error(true)
                    .message(e.getMessage())
                    .build();
        }
        return DtResponse.builder()
                .error(false)
                .data(orderList)
                .build();
    }

    @GetMapping("/{order_id}")
    public DtResponse getOrder(@PathVariable("order_id") Long order_id) {
        try {
            return DtResponse.builder()
                    .error(false)
                    .data(orderService.getOrder(order_id))
                    .build();
        } catch (OrderNotExistException e) {
            return DtResponse.builder()
                    .error(true)
                    .message(e.getMessage())
                    .build();
        }
    }

    @PostMapping
    public DtResponse purchaseProducts(@RequestBody DtOrder order) {
        try {
            String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            orderService.buyProducts(userEmail, order);
        } catch (BranchNotExistException | ProductNotExistException | UserNotExistException | NoStockException | InvalidOrderException | AddressNotExistException e) {
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

    @PutMapping("/employee")
    public DtResponse updateOrderStatus(@RequestBody DtOrder order) {
        try {
            String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            orderService.updateStatus(userEmail, order);
        } catch (EmployeeNotWorksInException | InvalidOrderException | OrderNotExistException e) {
            return DtResponse.builder()
                    .error(true)
                    .message(e.getMessage())
                    .build();
        }

        return DtResponse.builder()
                .error(false)
                .message("Estado de orden " + order.getId() + " actualizada: " + order.getStatus().name())
                .build();
    }

    @PutMapping("/{order_id}")
    public DtResponse cancelOrder(@PathVariable("order_id") Long order_id) {
        try {
            String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            orderService.cancelOrder(userEmail, order_id);
        } catch (InvalidOrderException | OrderNotExistException e) {
            return DtResponse.builder()
                    .error(true)
                    .message(e.getMessage())
                    .build();
        }

        return DtResponse.builder()
                .error(false)
                .message("Orden cancelada")
                .build();
    }

    @PostMapping("/issues")
    public DtResponse fileOrderComplaint(@RequestBody DtIssue dtIssue) {
        try {
            String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            issueService.addComplaint(userEmail, dtIssue);
        } catch (InvalidOrderException | IllegalArgumentException | OrderNotExistException | UserNotExistException e) {
            return DtResponse.builder()
                    .error(true)
                    .message(e.getMessage())
                    .build();
        }
        return DtResponse.builder()
                .error(false)
                .message("Reclamo realizado. Nos contactaremos a la brevedad.")
                .build();
    }
}
