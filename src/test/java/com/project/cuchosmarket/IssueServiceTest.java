package com.project.cuchosmarket;

import com.project.cuchosmarket.dto.DtIssue;
import com.project.cuchosmarket.dto.DtOrder;
import com.project.cuchosmarket.exceptions.EmployeeNotWorksInException;
import com.project.cuchosmarket.exceptions.InvalidOrderException;
import com.project.cuchosmarket.exceptions.OrderNotExistException;
import com.project.cuchosmarket.exceptions.UserNotExistException;
import com.project.cuchosmarket.models.*;
import com.project.cuchosmarket.repositories.BranchRepository;
import com.project.cuchosmarket.repositories.EmployeeRepository;
import com.project.cuchosmarket.repositories.OrderRepository;
import com.project.cuchosmarket.repositories.UserRepository;
import com.project.cuchosmarket.services.IssueService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.postgresql.hostchooser.HostRequirement.any;

@SpringBootTest
@AutoConfigureMockMvc
public class IssueServiceTest {
    @Autowired
    private IssueService issueService;
    @MockBean
    private BranchRepository branchRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private OrderRepository orderRepository;
    @MockBean
    private EmployeeRepository employeeRepository;
    @Test
    public void testAddComplaint() throws InvalidOrderException, OrderNotExistException, UserNotExistException {
        String userEmail = "adri@email.com";
        DtIssue dtIssue = new DtIssue("titulo", "descriptions", LocalDate.now(), "adri", 1l);
        /////
        Customer user = new Customer();
        user.setEmail(userEmail);
        when(userRepository.findByEmail(userEmail)).thenReturn(user);

        Order order = new Order();
        order.setCustomer(user);
        order.setId(1l);
        order.setEndDate(LocalDate.now());
        List<Order> orders = new ArrayList<>();
        orders.add(order);
        when(orderRepository.findById(1l)).thenReturn(Optional.of(order));
        user.setId(2l);

        Issue issue = new Issue(dtIssue.getTitle(), dtIssue.getDescription(), userEmail, LocalDate.now(), order);
        List<Issue> listI = new ArrayList<>();
        listI.add(issue);
        Branch branch = new Branch();

        branch.setOrders(orders);
        branch.setIssues(listI);
        when(branchRepository.findByOrdersContains(order)).thenReturn(branch);

        issueService.addComplaint(userEmail,dtIssue);
    }
    @Test
    public void testGetIssuesByBranch() throws EmployeeNotWorksInException {
        String userEmail = "adri@email.com";
        Long branchId = 2l;
        String orderDirection = "calle 1234";
        int pageNumber = 2;
        int pageSize = 2;
        Employee employee = new Employee();
        employee.setId(3l);
        Branch branch = new Branch();
        branch.setId(2l);
        employee.setBranch(branch);
        when(userRepository.findByEmail(userEmail)).thenReturn(employee);
        when(employeeRepository.findById(3l)).thenReturn(Optional.of(employee));

        List<DtIssue> expectedIssues = new ArrayList<>();
        Page<DtIssue> entrada = new PageImpl<>(expectedIssues);
        when(branchRepository.findIssues(eq(branchId), any(Pageable.class))).thenReturn(entrada);
        Page<DtIssue> salida = issueService.getIssuesByBranch(userEmail,branchId,orderDirection,pageNumber,pageSize);
        assertNotNull(salida);

    }
}