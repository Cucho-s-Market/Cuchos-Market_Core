package com.project.cuchosmarket.services;

import com.project.cuchosmarket.dto.DtAddress;
import com.project.cuchosmarket.exceptions.AddressNotExistException;
import com.project.cuchosmarket.exceptions.InvalidAddressException;
import com.project.cuchosmarket.exceptions.UserNotExistException;
import com.project.cuchosmarket.models.Address;
import com.project.cuchosmarket.models.Customer;
import com.project.cuchosmarket.models.User;
import com.project.cuchosmarket.repositories.AddressRepository;
import com.project.cuchosmarket.repositories.CustomerRepository;
import com.project.cuchosmarket.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AddressService {
    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    private Address findAddress(DtAddress dtAddress,Long id) throws AddressNotExistException {
        Optional<Address> address = addressRepository.findById(dtAddress.getId());

        if(address.isEmpty()) throw new AddressNotExistException("Direccion no encontrada.");

        return address.get();
    }

    private Customer validateCustomer(String userEmail) throws UserNotExistException {
        User user = userRepository.findByEmail(userEmail);
        if (user == null) throw new UserNotExistException();
        return customerRepository.findById(user.getId()).orElseThrow(UserNotExistException::new);
    }

    public void addAddress(DtAddress dtAddress,String userEmail) throws UserNotExistException, InvalidAddressException {
        Customer customer = validateCustomer(userEmail);
        if(dtAddress.getAddress() == null || dtAddress.getLocation() == null || dtAddress.getState() == null) throw new InvalidAddressException();

        Address address = new Address(dtAddress.getAddress(), dtAddress.getDoorNumber(), dtAddress.getLocation(), dtAddress.getState());

        customer.addAddress(address);
        customerRepository.save(customer);
    }

    public void updateAddress(String userEmail, DtAddress dtAddress) throws UserNotExistException, AddressNotExistException {
        Customer customer = validateCustomer(userEmail);

        Address address = findAddress(dtAddress, customer.getId());
        address.setAddress(dtAddress.getAddress());
        address.setDoorNumber(dtAddress.getDoorNumber());
        address.setLocation(dtAddress.getLocation());
        address.setState(dtAddress.getState());

        addressRepository.save(address);
    }

    public void deleteAddress(String userEmail, DtAddress dtAddress) throws UserNotExistException, AddressNotExistException {
        Customer customer = validateCustomer(userEmail);

        if(!customer.removeAddress(dtAddress.getId())) throw new AddressNotExistException();

        userRepository.save(customer);
    }

  }
