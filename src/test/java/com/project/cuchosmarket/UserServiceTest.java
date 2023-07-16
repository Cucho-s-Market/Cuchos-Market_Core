package com.project.cuchosmarket;

import com.project.cuchosmarket.dto.DtCustomer;
import com.project.cuchosmarket.dto.DtUser;
import com.project.cuchosmarket.exceptions.BranchNotExistException;
import com.project.cuchosmarket.exceptions.CustomerDisabledException;
import com.project.cuchosmarket.exceptions.UserExistException;
import com.project.cuchosmarket.exceptions.UserNotExistException;
import com.project.cuchosmarket.models.Branch;
import com.project.cuchosmarket.models.Customer;
import com.project.cuchosmarket.models.Employee;
import com.project.cuchosmarket.models.User;
import com.project.cuchosmarket.repositories.*;
import com.project.cuchosmarket.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;
    @MockBean
    private EmployeeRepository employeeRepository;
    @MockBean
    private AdminRepository adminRepository;
    @MockBean
    private CustomerRepository customerRepository;
    @MockBean
    private BranchRepository branchRepository;

    @MockBean
    private AuthenticationManager authenticationManager;

    private Employee employee;
    private Customer customer;

    @BeforeEach
    void setUp() {
        employee = new Employee("Tomas", "Lopez", "tommy@mail.com","password", Branch.builder()
                .id(1L).build());
        customer = new Customer("Federica", "Garcia", "gracia.fede@mail.com", "password",
                null, 598682593, 52003048);
    }

    @DisplayName("Listar usuarios")
    @Test
    public void testGetListUsers() {
        List<User> users = List.of(employee, customer);

        when(userRepository.findAll()).thenReturn(users);

        List<DtUser> resultado = userService.getUsers();

        assertEquals(resultado.size(),users.size());
        assertEquals(resultado.get(0).getEmail(), users.get(0).getEmail());
    }

    @DisplayName("Iniciar sesion correctamente")
    @Test
    public void testLogin() throws UserNotExistException, CustomerDisabledException {
        DtUser dtUser = new DtUser(null, null, null,"tommy@mail.com", "password", null);

        when(userRepository.findByEmail(any())).thenReturn(employee);
        when(authenticationManager.authenticate(any()))
                .thenReturn(new UsernamePasswordAuthenticationToken(dtUser.getEmail(), dtUser.getPassword()));

        assertNotNull(userService.authenticate(dtUser));
    }

    @DisplayName("Iniciar sesion: Cliente bloqueado")
    @Test
    public void testLoginDisableClient() throws UserNotExistException, CustomerDisabledException {
        DtUser dtUser = new DtUser(null, null, null, "gracia.fede@mail.com", "password", null);
        customer.setDisabled(true);

        when(userRepository.findByEmail(any())).thenReturn(customer);
        when(customerRepository.findById(any())).thenReturn(Optional.of(customer));

        assertThrows(CustomerDisabledException.class, () -> userService.authenticate(dtUser));
    }

    @DisplayName("Agregar empleado")
    @Test
    public void testAddEmployee() throws BranchNotExistException, UserExistException {
        DtUser dtUser = new DtUser(1L,"Tomas", "Lopez", "tommy@mail.com","password", "EMPLOYEE");

        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(branchRepository.findById(any())).thenReturn(Optional.ofNullable(Branch.builder().id(1L).build()));

        userService.addEmployee(1L, dtUser);

        verify(employeeRepository, times(1)).save(any());
    }

    @DisplayName("Empleado ya existe")
    @Test
    public void testAddEmployeeAlreadyExist() {
        DtUser dtUser = new DtUser(1L,"Tomas", "Lopez", "tommy@mail.com","password", "EMPLOYEE");

        when(userRepository.existsByEmail(any())).thenReturn(true);
        assertThrows(UserExistException.class, () -> userService.addEmployee(1L, dtUser));
    }

    @DisplayName("Agregar cliente")
    @Test
    public void testAddCustomer() throws UserExistException {
        DtCustomer dtUser = new DtCustomer(2L, "Federica", "Garcia", "gracia.fede@mail.com", "password",
                null, false);
        dtUser.setDni(52003048);
        dtUser.setTelephone(598682593);

        when(userRepository.existsByEmail(any())).thenReturn(false);

        userService.addCustomer(dtUser);

        verify(customerRepository, times(1)).save(any());
    }

    @DisplayName("Agregar cliente: Invalido")
    @Test
    public void testAddCustomerInvalid() throws UserExistException {
        DtCustomer dtUser = new DtCustomer(2L, "Federica", "Garcia", "gracia.fede@mail.com", "password",
                null, false);
        dtUser.setDni(52003048);
        dtUser.setTelephone(598682593);

        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(customerRepository.existsByDni(52003048)).thenReturn(true);

        assertThrows(UserExistException.class, () -> userService.addCustomer(dtUser));
    }

    @DisplayName("Editar usuario")
    @Test
    public void testUpdateUser() throws UserExistException, UserNotExistException {
        DtCustomer dtUser = new DtCustomer(2L, "Federica", "Garcia", "gracia.fede@mail.com", "password",
                null, false);
        dtUser.setDni(52003048);
        dtUser.setTelephone(598682593);

        when(userRepository.findByEmail("gracia.fede@mail.com")).thenReturn(customer);
        when(customerRepository.findById(any())).thenReturn(Optional.ofNullable(customer));

        userService.updateUser("gracia.fede@mail.com", dtUser);

        verify(customerRepository, times(1)).save(any());
        verify(userRepository, times(1)).save(any());
    }

    @DisplayName("Borrar usuario")
    @Test
    public void testDeleteUser() throws UserNotExistException {
        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(customer));
        userService.deleteUser(any());

        verify(customerRepository, times(1)).deleteById(any());
        verify(userRepository, times(1)).delete(any());
    }

    @DisplayName("Bloquear cliente")
    @Test
    public void testdisableCustomer() throws UserNotExistException {
        DtCustomer dtUser = new DtCustomer(2L, "Federica", "Garcia", "gracia.fede@mail.com", "password",
                null, false);
        dtUser.setDni(52003048);
        dtUser.setTelephone(598682593);

        when(userRepository.findByEmail("gracia.fede@mail.com")).thenReturn(customer);
        when(customerRepository.findById(any())).thenReturn(Optional.ofNullable(customer));

        userService.disableCustomer(dtUser);

        verify(customerRepository, times(1)).save(any());
    }
}


