package com.project.cuchosmarket;

import com.project.cuchosmarket.enums.Role;
import com.project.cuchosmarket.exceptions.BranchNotExistException;
import com.project.cuchosmarket.exceptions.UserExistException;
import com.project.cuchosmarket.models.Branch;
import com.project.cuchosmarket.models.Customer;
import com.project.cuchosmarket.models.Employee;
import com.project.cuchosmarket.models.User;
import com.project.cuchosmarket.repositories.*;

import com.project.cuchosmarket.security.JwtService;
import com.project.cuchosmarket.security.UserDetailsImpl;
import com.project.cuchosmarket.services.UserService;
import org.junit.*;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.*;

import com.project.cuchosmarket.dto.*;


import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.*;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;


import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.ExpectedCount.never;
import static org.springframework.test.web.client.ExpectedCount.times;


@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private BranchRepository branchRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

     @Mock
       private JwtService jwtService;

    @InjectMocks
    private UserService userService;

    @Test
    public void testAddEmployee() throws BranchNotExistException, UserExistException {
         Long branchId = 1L;
        DtUser dtUser = new DtUser(1l, "John", "Doe", "john.doe@example.com", "password123", "admin");

        Branch branch = new Branch();
        Mockito.when(branchRepository.findById(branchId)).thenReturn(Optional.of(branch));

        Mockito.when(userRepository.existsByEmail(Mockito.anyString())).thenReturn(false);

        Employee savedEmployee = new Employee();
        Mockito.when(employeeRepository.save(Mockito.any(Employee.class))).thenReturn(savedEmployee);

        userService.addEmployee(branchId, dtUser);

        Mockito.verify(branchRepository).findById(branchId);
        Mockito.verify(userRepository).existsByEmail(dtUser.getEmail());
        Mockito.verify(employeeRepository).save(Mockito.any(Employee.class));
    }

    @Test
    public void testAddCustomer() throws UserExistException {
        // Arrange
        DtCustomer dtCustomer = new DtCustomer(1l, "Doe", "johndoe@example.com", "password123", "admin","admin");
        dtCustomer.setTelephone(123456789L);
        dtCustomer.setDni(987654321L);

        when(customerRepository.existsByDni(dtCustomer.getDni())).thenReturn(false);
        when(passwordEncoder.encode(dtCustomer.getPassword())).thenReturn("encodedPassword");
        when(jwtService.createToken(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn("jwtToken");

        ArgumentCaptor<Customer> customerCaptor = ArgumentCaptor.forClass(Customer.class);

        // Act
        DtResponse response = userService.addCustomer(dtCustomer);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getToken());
        assertNull(response.getData());

        verify(customerRepository).save(customerCaptor.capture());

        Customer savedCustomer = customerCaptor.getValue();
        assertNotNull(savedCustomer);
        assertEquals(dtCustomer.getFirstName(), savedCustomer.getFirstName());
        assertEquals(dtCustomer.getLastName(), savedCustomer.getLastName());
        assertEquals(dtCustomer.getEmail(), savedCustomer.getEmail());
        assertEquals("encodedPassword", savedCustomer.getPassword());
        assertNull(savedCustomer.getBirthdate());
        assertEquals(dtCustomer.getTelephone(), savedCustomer.getTelephone());
        assertEquals(dtCustomer.getDni(), savedCustomer.getDni());
    }
    @Test
    public void testGetUsers() {
        // Arrange
        List<User> users = new ArrayList<>();
        users.add(new Employee( "John", "Doe", "john@example.com", "password", new Branch()));
        users.add(new Customer( "Jane", "Smith", "jane@example.com", "password", null, 123456789L, 1234567890L));

        when(userRepository.findAll()).thenReturn(users);

        // Act
        List<DtUser> dtUsers = userService.getUsers();

        Mockito.verify(userRepository).findAll();

        // Assert
        assertEquals(users.size(), dtUsers.size());

        DtUser dtUser1 = dtUsers.get(0);

        assertEquals("John", dtUser1.getFirstName());
        assertEquals("Doe", dtUser1.getLastName());
        assertEquals("john@example.com", dtUser1.getEmail());
        assertEquals(Role.EMPLOYEE.name(), dtUser1.getRole());

        DtUser dtUser2 = dtUsers.get(1);

        assertEquals("Jane", dtUser2.getFirstName());
        assertEquals("Smith", dtUser2.getLastName());
        assertEquals("jane@example.com", dtUser2.getEmail());
        assertEquals(Role.CUSTOMER.name(), dtUser2.getRole());

        for (DtUser dtusers : dtUsers) {
            System.out.printf("ID: %s%n", dtusers.getFirstName());
            System.out.printf("Nombre: %s%n", dtusers.getLastName());
            System.out.printf("Descripción: %s%n", dtusers.getEmail());
            System.out.printf("Imagen: %s%n", dtusers.getRole());
            System.out.println("----------------------");
        }
    }
    }












