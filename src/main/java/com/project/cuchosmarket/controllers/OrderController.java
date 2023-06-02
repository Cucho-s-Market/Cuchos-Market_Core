package com.project.cuchosmarket.controllers;

import com.project.cuchosmarket.dto.DtResponse;
import com.project.cuchosmarket.exceptions.EmployeeNotWorksInException;
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

    @GetMapping("/employee/{user_id}/get-orders/{branch_id}")
    public DtResponse getOrdersHistory(@PathVariable("user_id") Long user_id,
                                       @PathVariable("branch_id") Long branch_id,
                                       @RequestParam(value = "orderStatus", required = false) String orderStatus,
                                       @RequestParam(value = "startDate", required = false) LocalDate startDate,
                                       @RequestParam(value = "endDate", required = false) LocalDate endDate,
                                       @RequestParam(value = "orderDirection", required = false) String orderDirection) {
        List<Order> orderList = null;
        try {
            orderList = orderService.getOrdersBy(user_id, branch_id, orderStatus,
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
}
