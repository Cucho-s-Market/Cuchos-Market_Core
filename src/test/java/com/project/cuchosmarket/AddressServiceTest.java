package com.project.cuchosmarket;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AddressServiceTest {
    @MockBean
    private CustomerRepository customerRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private AddressRepository addressRepository;

    @Autowired
    private AddressService addressService;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer("Federica", "Garcia", "gracia.fede@mail.com", "password",
                null, 598682593, 52003048);
        List<Address> addresses = new ArrayList<>();
        addresses.add(new Address(1L, "Av. Importante", 101, "En algun lado", "Existe"));
        customer.setAddresses(addresses);
    }


    @DisplayName("Agregar direccion a cliente")
    @Test
    void testAddAddress() throws UserNotExistException, InvalidAddressException {
        DtAddress dtAddress = new DtAddress(1L, "Av. Importante", 101, "En algun lado", "Existe");

        when(userRepository.findByEmail(any())).thenReturn(customer);
        when(customerRepository.findById(any())).thenReturn(Optional.ofNullable(customer));

        addressService.addAddress(dtAddress, customer.getEmail());

        verify(customerRepository, times(1)).save(any());
    }

    @DisplayName("Direccion Invalida")
    @Test
    void testAddAddressInvalidAddressException() throws UserNotExistException, InvalidAddressException {
        DtAddress dtAddress = new DtAddress(1L, "", 101, "En algun lado", "Existe");

        when(userRepository.findByEmail(any())).thenReturn(customer);
        when(customerRepository.findById(any())).thenReturn(Optional.ofNullable(customer));

        assertThrows(InvalidAddressException.class, () -> addressService.addAddress(dtAddress, customer.getEmail()));
    }

    @DisplayName("Actualizar direccion")
    @Test
    void testUpdateAddress() throws AddressNotExistException, UserNotExistException, InvalidAddressException {
        DtAddress dtAddress = new DtAddress(1L, "Av. Importante", 101, "En algun lado", "Existe");

        when(userRepository.findByEmail(any())).thenReturn(customer);
        when(customerRepository.findById(any())).thenReturn(Optional.ofNullable(customer));
        when(addressRepository.findById(any())).thenReturn(Optional.of(new Address()));

        addressService.updateAddress(customer.getEmail(), dtAddress);

        verify(addressRepository, times(1)).save(any());
    }

    @DisplayName("Borrar direccion")
    @Test
    void testDeleteAddress() throws AddressNotExistException, UserNotExistException {
        when(userRepository.findByEmail(any())).thenReturn(customer);
        when(customerRepository.findById(any())).thenReturn(Optional.ofNullable(customer));

        addressService.deleteAddress(customer.getEmail(), 1L);

        verify(userRepository, times(1)).save(any());
    }
}