package com.project.cuchosmarket.services;

import com.project.cuchosmarket.dto.DtAddress;
import com.project.cuchosmarket.exceptions.UserNotExistExeption;
import com.project.cuchosmarket.models.Address;
import com.project.cuchosmarket.models.Customer;
import com.project.cuchosmarket.repositories.AddressRepository;
import com.project.cuchosmarket.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AddressService {
    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;


    public void  addAddress(DtAddress dtAddress,Long  id) throws UserNotExistExeption {
        Optional<Customer> custmer = customerRepository.findById(id);
        if(custmer.isEmpty()){
           throw new UserNotExistExeption("Usuario no existe");
        }
        if(dtAddress.getAddress().length() > 50 || dtAddress.getLocation() == null || dtAddress.getState() == null){
            throw new IllegalArgumentException("Datos invalidos");
        }
        Address address = new Address(dtAddress.getAddress(), dtAddress.getDoorNumber(), dtAddress.getLocation(), dtAddress.getState());

        custmer.get().addAddress(address);

        customerRepository.save(custmer.get());



    }
  }
