package com.project.cuchosmarket.controllers;

import com.project.cuchosmarket.dto.DtAddress;
import com.project.cuchosmarket.dto.DtResponse;
import com.project.cuchosmarket.exceptions.UserNotExistExeption;
import com.project.cuchosmarket.services.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/address")
public class AdressController {
    private final AddressService addressService;

    @PostMapping("/customer/{user_id}")
    public DtResponse addAddress(@PathVariable("user_id") Long user_id, @RequestBody DtAddress address) {
        try {
        addressService.addAddress(address, user_id);
        }  catch (UserNotExistExeption | IllegalArgumentException  e) {
            return DtResponse.builder()
                    .error(true)
                    .message(e.getMessage())
                    .build();

        }

        return DtResponse.builder()
                .error(false)
                .message("Direccion añadida con exito.")
                .build();
    }


}