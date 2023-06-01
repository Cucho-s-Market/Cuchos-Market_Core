package com.project.cuchosmarket.controllers;

import com.project.cuchosmarket.dto.DtAddress;
import com.project.cuchosmarket.dto.DtCustomer;
import com.project.cuchosmarket.dto.DtResponse;
import com.project.cuchosmarket.dto.DtUser;
import com.project.cuchosmarket.exceptions.*;
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

    @PostMapping("/auth/login")
    public DtResponse login(@RequestBody DtUser user) {
        DtResponse token;
        try {
            token = userService.authenticate(user);
        } catch (UserNotExistException e) {
            return DtResponse.builder()
                    .error(true)
                    .message(e.getMessage())
                    .build();
        }

        token.setError(false);
        return token;
    }

    @PostMapping("/add-employee/{branch_id}")
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

    @PostMapping("/add-customer")
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

    @GetMapping("/get-users")
    public DtResponse getUsers() {
        return DtResponse.builder()
                .error(false)
                .message(String.valueOf(userService.getUsers().size()))
                .data(userService.getUsers())
                .build();
    }

    @DeleteMapping("/delete-user/{user_id}")
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

    @PostMapping("/add-address/{user_id}")
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

    @PostMapping("/update-address/{user_id}")
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

    @DeleteMapping("/delete-address/{user_id}")
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
}
