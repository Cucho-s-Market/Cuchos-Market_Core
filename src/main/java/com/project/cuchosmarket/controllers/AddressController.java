package com.project.cuchosmarket.controllers;
import com.project.cuchosmarket.dto.DtAddress;
import com.project.cuchosmarket.dto.DtResponse;
import com.project.cuchosmarket.exceptions.AddressNotExistExeption;
import com.project.cuchosmarket.exceptions.UserNotExistExeption;
import com.project.cuchosmarket.services.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/address")
public class AddressController {
    private final AddressService addressService;

    @PostMapping("/add-address/{user_id}")
    public DtResponse addAddress(@PathVariable("user_id") Long id, @RequestBody DtAddress address) {
        try {
        addressService.addAddress(address, id);
        }  catch (UserNotExistExeption | IllegalArgumentException | AddressNotExistExeption e) {
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

    @DeleteMapping("/delete-address/{user_id}")
    public DtResponse deleteAddress(@PathVariable("user_id") Long id, @RequestBody DtAddress address) {
        try {
            addressService.deleteAddress(id, address);
        } catch (UserNotExistExeption | AddressNotExistExeption e) {
            return DtResponse.builder()
                    .error(true)
                    .message(e.getMessage())
                    .build();
        }

        return DtResponse.builder()
                .error(false)
                .message("Direccion eliminada con exito.")
                .build();
    }
}
