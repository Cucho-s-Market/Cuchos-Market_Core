package com.project.cuchosmarket.services;

import com.project.cuchosmarket.enums.OrderStatus;
import com.project.cuchosmarket.exceptions.EmployeeNotWorksInException;
import com.project.cuchosmarket.models.MarketBranch;
import com.project.cuchosmarket.models.Order;
import com.project.cuchosmarket.models.User;
import com.project.cuchosmarket.repositories.EmployeeRepository;
import com.project.cuchosmarket.repositories.OrderRepository;
import com.project.cuchosmarket.repositories.UserRepository;
import com.project.cuchosmarket.repositories.specifications.OrderSpecifications;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Service
public class OrderService {
    private OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;


    public List<Order> getOrdersBy(String username, Long marketBranchId, String orderStatus, LocalDate startDate,
                                   LocalDate endDate, String orderDirection) throws EmployeeNotWorksInException {
        User user = userRepository.findByEmail(username);
        MarketBranch marketBranchEmployee = employeeRepository.findById(user.getId()).get().getMarketBranch();

        if (!marketBranchEmployee.getId().equals(marketBranchId)) throw new EmployeeNotWorksInException();

        OrderStatus status = null;
        if (orderStatus != null) {
            try {
                status = OrderStatus.valueOf(orderStatus.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Estado de la compra invalido.");
            }
        }

        Specification<Order> specification = OrderSpecifications.filterByAttributes(status, startDate, endDate, orderDirection);
        return orderRepository.findAll(specification);
    }
}
