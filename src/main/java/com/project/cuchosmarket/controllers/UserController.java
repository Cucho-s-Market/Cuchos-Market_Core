package com.project.cuchosmarket.controllers;

import com.project.cuchosmarket.dto.DtCustomer;
import com.project.cuchosmarket.dto.DtResponse;
import com.project.cuchosmarket.dto.DtUser;
import com.project.cuchosmarket.exceptions.CustomerExistExeption;
import com.project.cuchosmarket.exceptions.MarketBranchNotExist;
import com.project.cuchosmarket.exceptions.UserExistException;
import com.project.cuchosmarket.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/market_branch/{branch_id}/employee")
    public DtResponse addEmployee(@PathVariable("branch_id") Long branch_id, @RequestBody DtUser employee) {
        try {
            userService.addEmployee(branch_id, employee);
        } catch (MarketBranchNotExist | UserExistException | IllegalArgumentException e) {
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
    @PostMapping("/market_branch/{branch_dni}/customer")
    public DtResponse addCustomer(@PathVariable("branch_dni") Long branch_dni, @RequestBody DtCustomer customer) {
        try {
            userService.addCustomer(branch_dni, customer);
        }  catch (MarketBranchNotExist | UserExistException | IllegalArgumentException | CustomerExistExeption e) {
            return DtResponse.builder()
                    .error(true)
                    .message(e.getMessage())
                    .build();

        }

        return DtResponse.builder()
                .error(false)
                .message("Cliente añadido con exito.")
                .build();
    }
}
