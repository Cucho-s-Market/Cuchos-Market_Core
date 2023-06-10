package com.project.cuchosmarket;


import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;
import static  org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import com.project.cuchosmarket.models.Customer;
import com.project.cuchosmarket.models.User;
import com.project.cuchosmarket.repositories.CustomerRepository;
import com.project.cuchosmarket.repositories.UserRepository;
import com.project.cuchosmarket.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.verification.VerificationMode;
import org.springframework.boot.env.ConfigTreePropertySource;
import org.junit.jupiter.api.Test;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;
    @Mock
    private CustomerRepository customerRepository;

    private Customer customer;
    private  User user;
    @BeforeEach
    void setup() {
    /*    customer = new Customer();
        customer.setFirstName("Carlos");
        customer.setLastName("barrera");
        customer.setEmail("cucho@gmail");
        customer.setPassword("123");
        customer.setTelephone(123456);
        customer.setDni(321);
*/
        user = new Customer();
        user.setFirstName("Carlos");
        user.setLastName("barrera");
        user.setEmail("cucho@gmail");
        user.setPassword("123");

        // Configuración del comportamiento simulado del repositorio
/*
        when(userRepository.findByEmail(customer.getEmail())).thenReturn(null); // El correo no existe previamente
        when(userRepository.save(customer)).thenReturn(customer); // El empleado se guarda correctamente

        // Llamada al método que se está probando
        Customer savedCustomer = customerRepository.save(customer);

        // Verificaciones
        assertNotNull(savedCustomer); // Se guarda correctamente

        verify(userRepository,times(1)).findByEmail(customer.getEmail()); // Se verifica la llamada al método findByEmail una vez
        verify(userRepository,times(1)).save(customer); //

    }
*/
    }
   @DisplayName("Test guardar customer")
   @Test
    void testAddCustomer(){

        given(userRepository.findByEmail(user.getEmail()))
                .willReturn(user);
        given(userRepository.save(user)).willReturn(user);

       User savedCustomer = userRepository.save(user);
    }

}
