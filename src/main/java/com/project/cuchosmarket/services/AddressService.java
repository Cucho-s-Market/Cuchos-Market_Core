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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AddressService {
    private final CustomerRepository customerRepository;
    @Autowired
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

    public List<DtAddress> getAddress(String userEmail) throws UserNotExistException {
        // Validating user
        Customer customer = validateCustomer(userEmail);
        if(customer == null) throw new UserNotExistException();

        List<DtAddress> dtAddresses = new ArrayList<>();
        List<Address> addresses = customer.getAddresses();

  addresses.forEach(address ->
                dtAddresses.add(new DtAddress(address.getId(), address.getAddress(), address.getDoorNumber(), address.getLocation(), address.getState())));


        return dtAddresses;
    }

    public void addAddress(DtAddress dtAddress,String userEmail) throws UserNotExistException, InvalidAddressException, AddressNotExistException {
        Customer customer = validateCustomer(userEmail);
        if(dtAddress.getAddress() == null || dtAddress.getAddress().length() > 50
                || dtAddress.getLocation() == null || dtAddress.getState() == null)

            throw new InvalidAddressException();


        Address address = new Address(dtAddress.getAddress(), dtAddress.getDoorNumber(), dtAddress.getLocation(), dtAddress.getState());

        customer.addAddress(address);
        customerRepository.save(customer);
        if (addressRepository.findById(address.getId()) == null) {

            throw new AddressNotExistException();
        }
    }

    public void updateAddress(String userEmail, DtAddress dtAddress) throws UserNotExistException, AddressNotExistException, InvalidAddressException {
        Customer customer = validateCustomer(userEmail);
        if(dtAddress.getAddress() == null || dtAddress.getAddress().length() > 50
                || dtAddress.getLocation() == null || dtAddress.getState() == null) throw new InvalidAddressException();

        Address address = findAddress(dtAddress, customer.getId());
        address.setAddress(dtAddress.getAddress());
        address.setDoorNumber(dtAddress.getDoorNumber());
        address.setLocation(dtAddress.getLocation());
        address.setState(dtAddress.getState());

        addressRepository.save(address);
    }

    public void deleteAddress(String userEmail, Long address_id) throws UserNotExistException, AddressNotExistException {
        Customer customer = validateCustomer(userEmail);

        if(!customer.removeAddress(address_id)) throw new AddressNotExistException();

        userRepository.save(customer);
    }

  }
