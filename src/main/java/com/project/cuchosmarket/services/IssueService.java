package com.project.cuchosmarket.services;

import com.project.cuchosmarket.dto.DtIssue;
import com.project.cuchosmarket.exceptions.EmployeeNotWorksInException;
import com.project.cuchosmarket.exceptions.InvalidOrderException;
import com.project.cuchosmarket.exceptions.OrderNotExistException;
import com.project.cuchosmarket.exceptions.UserNotExistException;
import com.project.cuchosmarket.models.Branch;
import com.project.cuchosmarket.models.Issue;
import com.project.cuchosmarket.models.Order;
import com.project.cuchosmarket.models.User;
import com.project.cuchosmarket.repositories.BranchRepository;
import com.project.cuchosmarket.repositories.EmployeeRepository;
import com.project.cuchosmarket.repositories.OrderRepository;
import com.project.cuchosmarket.repositories.UserRepository;
import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@Service
public class IssueService {
    private final OrderRepository orderRepository;
    private final BranchRepository branchRepository;
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;

    private boolean havePassed24Hours(LocalDate date) {
        LocalDateTime dateTime = date.atStartOfDay();
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime endDatePlus24Hours = dateTime.plusDays(1);
        return now.isAfter(endDatePlus24Hours);
    }

    public void addComplaint(String userEmail, DtIssue dtIssue) throws OrderNotExistException, IllegalArgumentException, UserNotExistException, InvalidOrderException {
        Order order = orderRepository.findById(dtIssue.getOrderId()).orElseThrow(() -> new OrderNotExistException(dtIssue.getOrderId()));
        if (order.getEndDate() == null || havePassed24Hours(order.getEndDate())) throw new IllegalArgumentException("No es posible realizar " +
                "un reclamo de este pedido. No cumple con nuestras politicas de reclamo de compras.");

        User user = userRepository.findByEmail(userEmail);
        if (user == null) throw new UserNotExistException();
        else if (!order.getCustomer().getId().equals(user.getId())) throw new InvalidOrderException("Este pedido no pertence al cliente.");

        if(dtIssue.getTitle().isBlank() || dtIssue.getTitle().length() > 50) throw new IllegalArgumentException("Datos invalidos.");
        if(StringUtils.isBlank(dtIssue.getDescription()) || dtIssue.getDescription().length() > 255
                || dtIssue.getDescription().length() < 5) throw new IllegalArgumentException("Datos invalidos.");


        Issue issue = new Issue(dtIssue.getTitle(), dtIssue.getDescription(), userEmail, LocalDate.now(), order);

        Branch branch = branchRepository.findByOrdersContains(order);
        branch.addIssue(issue);
        branchRepository.save(branch);
    }

    private void employeeWorksInBranch(String username, Long marketBranchId) throws EmployeeNotWorksInException {
        User user = userRepository.findByEmail(username);
        Branch branchEmployee = employeeRepository.findById(user.getId()).get().getBranch();

        if (!branchEmployee.getId().equals(marketBranchId)) throw new EmployeeNotWorksInException();
    }

    private Sort createSortBy(String orderDirection) {
        return (orderDirection == null || orderDirection.equalsIgnoreCase("desc")) ?
                Sort.by("i.creationDate").descending() :
                Sort.by("i.creationDate").ascending();
    }

    public Page<DtIssue> getIssuesByBranch(String userEmail, Long branchId, String orderDirection, int pageNumber, int pageSize) throws EmployeeNotWorksInException {
        employeeWorksInBranch(userEmail, branchId);

        Pageable pageable = PageRequest.of(pageNumber, pageSize, createSortBy(orderDirection));
        return branchRepository.findIssues(branchId, pageable);
    }
}
