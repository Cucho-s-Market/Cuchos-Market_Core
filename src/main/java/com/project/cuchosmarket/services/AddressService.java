package com.project.cuchosmarket.services;

import com.project.cuchosmarket.dto.DtAddress;
import com.project.cuchosmarket.exceptions.AddressNotExistExeption;
import com.project.cuchosmarket.exceptions.UserNotExistExeption;
import com.project.cuchosmarket.models.Address;
import com.project.cuchosmarket.models.Customer;
import com.project.cuchosmarket.models.MarketBranch;
import com.project.cuchosmarket.repositories.AddressRepository;
import com.project.cuchosmarket.repositories.CustomerRepository;
import com.project.cuchosmarket.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.Store;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AddressService {
    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;


    private Address findAddress(DtAddress dtAddress,Long id) throws AddressNotExistExeption{
        Optional<Address> address = addressRepository.findById(dtAddress.getAddress());
        if(address.isEmpty()) {
            throw new AddressNotExistExeption("no se encuentra la direccion");
        }

        return address.get();
    }

    public void  addAddress(DtAddress dtAddress,Long  id) throws UserNotExistExeption, AddressNotExistExeption {
        Optional<Customer> custmer = customerRepository.findById(id);
        if(custmer.isEmpty()){
           throw  new UserNotExistExeption("Usuario no existe");
        }
        if(dtAddress.getAddress().length() > 50 || dtAddress.getLocation() == null || dtAddress.getState() == null){
            throw  new IllegalArgumentException("Datos invalidos");
        }



        Address address = new Address(dtAddress.getAddress(), dtAddress.getDoorNumber(), dtAddress.getLocation(), dtAddress.getState());

        custmer.get().addAddress(address);

        customerRepository.save(custmer.get());


    }
    public void  deleteAddress(DtAddress dtAddress,Long  id) throws UserNotExistExeption, AddressNotExistExeption {
        Optional<Customer> custmer = customerRepository.findById(id);
        if(custmer.isEmpty()){ throw  new UserNotExistExeption("Usuario no existe");  }

        List<Address> addresses = custmer.get().getAddresses();
        addresses.forEach(address -> {
            if (address.getId().equals(dtAddress.getId())) {
                addresses.remove(address);
            }
        });
        userRepository.save(custmer.get());
    }

  }
