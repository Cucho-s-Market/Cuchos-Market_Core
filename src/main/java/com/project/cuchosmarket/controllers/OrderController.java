package com.project.cuchosmarket.controllers;

import com.project.cuchosmarket.dto.DtOrder;
import com.project.cuchosmarket.dto.DtResponse;
import com.project.cuchosmarket.exceptions.BranchNotExistException;
import com.project.cuchosmarket.exceptions.EmployeeNotWorksInException;
import com.project.cuchosmarket.exceptions.ProductNotExistException;
import com.project.cuchosmarket.exceptions.UserNotExistException;
import com.project.cuchosmarket.models.Order;
import com.project.cuchosmarket.security.JwtService;
import com.project.cuchosmarket.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final JwtService jwtService;

    @GetMapping("/employee/get-orders/{branch_id}")
    public DtResponse getOrdersHistory(@RequestHeader("Authorization") String authorizationHeader,
                                       @PathVariable("branch_id") Long branch_id,
                                       @RequestParam(value = "orderStatus", required = false) String orderStatus,
                                       @RequestParam(value = "startDate", required = false) LocalDate startDate,
                                       @RequestParam(value = "endDate", required = false) LocalDate endDate,
                                       @RequestParam(value = "orderDirection", required = false) String orderDirection) {
        List<Order> orderList = null;
        try {
            String userEmail = jwtService.extractUsername(authorizationHeader.substring(7));
            orderList = orderService.getOrdersBy(userEmail, branch_id, orderStatus,
                    startDate, endDate, orderDirection);
        } catch (EmployeeNotWorksInException | IllegalArgumentException e) {
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

    @PostMapping("/customer/{user_id}/buy-products")
    public DtResponse purchaseProducts(@PathVariable("user_id") Long user_id, @RequestBody DtOrder order) {
        try {
            orderService.buyProducts(user_id, order);
        } catch (BranchNotExistException | ProductNotExistException | UserNotExistException e) {
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
}
