package com.project.cuchosmarket;
import static org.mockito.ArgumentMatchers.any;
import com.project.cuchosmarket.dto.DtAddress;
import com.project.cuchosmarket.exceptions.AddressNotExistException;
import com.project.cuchosmarket.exceptions.InvalidAddressException;
import com.project.cuchosmarket.exceptions.UserNotExistException;
import com.project.cuchosmarket.models.Address;
import com.project.cuchosmarket.models.Customer;
import com.project.cuchosmarket.repositories.AddressRepository;
import com.project.cuchosmarket.repositories.CustomerRepository;
import com.project.cuchosmarket.repositories.UserRepository;
import com.project.cuchosmarket.services.AddressService;
import com.project.cuchosmarket.services.UserService;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@SpringBootTest
@RunWith(SpringRunner.class)
public class AddressServiceTest {
    @Mock
    private AddressRepository addressRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserService userService;


    @InjectMocks
    private AddressService addressService;


    @Test
    public void testGetAddress() throws UserNotExistException {
        Address address = new Address(1L, "8 de octubre", 1, "union", "Montevideo");
        String userEmail = "sample@example.com";

        Customer customer = Mockito.mock(Customer.class);
        when(userRepository.findByEmail(userEmail)).thenReturn(customer);
        when(customerRepository.findById(any(Long.class))).thenReturn(Optional.of(customer));

        List<Address> addresses = new ArrayList<>();
        addresses.add(address);
        when(customer.getAddresses()).thenReturn(addresses);

        List<DtAddress> result = addressService.getAddress(userEmail);
        assertNotNull(result);
        assertEquals(1, result.size());

        DtAddress dtAddress = result.get(0);
        System.out.printf("ID: %d%n", dtAddress.getId());
        System.out.printf("Nombre: %s%n", dtAddress.getAddress());
        System.out.printf("Numero puerta: %s%n", dtAddress.getDoorNumber());
        System.out.printf("State: %s%n", dtAddress.getState());
        System.out.println("----------------------");
    }

    @Test
    public void testAddAddress() throws UserNotExistException, InvalidAddressException, AddressNotExistException {
        // Datos de prueba
        DtAddress dtAddress = new DtAddress(1L, "Main St", 1, "City", "State");
        String userEmail = "sample@example.com";

        // Mock de UserRepository
        Customer customer = Mockito.mock(Customer.class);
        when(userRepository.findByEmail(userEmail)).thenReturn(customer);

        // Mock de CustomerRepository
        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));

        // Llamada al método addAddress
        addressService.addAddress(dtAddress, userEmail);

        // Verificaciones
        verify(customer, times(1)).addAddress(any(Address.class));
        verify(customerRepository, times(1)).save(customer);

        Assertions.assertEquals(dtAddress.getId(), 1L);




    }
}









