package com.project.cuchosmarket.controllers;

import com.project.cuchosmarket.dto.DtAddress;
import com.project.cuchosmarket.dto.DtCustomer;
import com.project.cuchosmarket.dto.DtResponse;
import com.project.cuchosmarket.dto.DtUser;
import com.project.cuchosmarket.exceptions.AddressNotExistExeption;
import com.project.cuchosmarket.exceptions.BranchNotExistException;
import com.project.cuchosmarket.exceptions.UserExistException;
import com.project.cuchosmarket.exceptions.UserNotExistException;
import com.project.cuchosmarket.services.AddressService;
import com.project.cuchosmarket.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final AddressService addressService;
    
    @GetMapping
    public DtResponse getUsers() {
        return DtResponse.builder()
                .error(false)
                .message(String.valueOf(userService.getUsers().size()))
                .data(userService.getUsers())
                .build();
    }

    @PostMapping("/auth/login")
    public DtResponse login(@RequestBody DtUser user) {
        DtResponse token;
        try {
            token = userService.authenticate(user);
        } catch (UserNotExistException | CustomerDisabledException e) {
            return DtResponse.builder()
                    .error(true)
                    .message(e.getMessage())
                    .build();
        }

        token.setError(false);
        return token;
    }

    @PostMapping("/employee/{branch_id}")
    public DtResponse addEmployee(@PathVariable("branch_id") Long branch_id, @RequestBody DtUser employee) {
        try {
            userService.addEmployee(branch_id, employee);
        } catch (BranchNotExistException | UserExistException | IllegalArgumentException e) {
            return DtResponse.builder()
                    .error(true)
                    .message(e.getMessage())
                    .build();
        }

        return DtResponse.builder()
                .error(false)
                .message("Usuario añadido con exito.")
                .build();
    }

    @PostMapping("/customer")
    public DtResponse addCustomer(@RequestBody DtCustomer customer) {
        DtResponse response;
        try {
            response = userService.addCustomer(customer);
        }  catch ( UserExistException | IllegalArgumentException  e) {
            return DtResponse.builder()
                    .error(true)
                    .message(e.getMessage())
                    .build();

        }

        response.setError(false);
        response.setMessage("Cliente añadido con exito.");
        return response;
    }


    @DeleteMapping("/{user_id}")
    public DtResponse deleteUser(@PathVariable("user_id") Long user_id) {
        try {
            userService.deleteUser(user_id);
        } catch (UserNotExistException e) {
            return DtResponse.builder()
                    .error(true)
                    .message(e.getMessage())
                    .build();
        }
        return DtResponse.builder()
                .error(false)
                .message("Usuario eliminado con exito.")
                .build();
    }

    @PostMapping("/customer/{user_id}/address")
    public DtResponse addAddress(@PathVariable("user_id") Long id, @RequestBody DtAddress address) {
        try {
            addressService.addAddress(address, id);
        }  catch (UserNotExistException | IllegalArgumentException | AddressNotExistExeption e) {
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

    @PutMapping("/customer/{user_id}/address")
    public DtResponse updateAddress(@PathVariable("user_id") Long id, @RequestBody DtAddress address) {
        try {
            addressService.updateAddress(id, address);
        }
        catch (UserNotExistException | AddressNotExistExeption e) {
            return DtResponse.builder()
                    .error(true)
                    .message(e.getMessage())
                    .build();
        }

        return DtResponse.builder()
                .error(false)
                .message("Direccion actualizada con exito.")
                .build();
    }

    @DeleteMapping("/customer/{user_id}/address")
    public DtResponse deleteAddress(@PathVariable("user_id") Long id, @RequestBody DtAddress address) {
        try {
            addressService.deleteAddress(id, address);
        } catch (UserNotExistException | AddressNotExistExeption e) {
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

    @PutMapping("/admin/disable-customer")
    public DtResponse disableCustomer(@RequestBody DtCustomer customer) {
        try {
            userService.disableCustomer(customer);
        } catch (UserNotExistException e) {
            return DtResponse.builder()
                    .error(true)
                    .message(e.getMessage())
                    .build();
        }

        return DtResponse.builder()
                .error(false)
                .message("Usuario " + (customer.isDisabled() ? "bloqueado" : "desbloqueado") + " con éxito")
                .build();
    }
}
