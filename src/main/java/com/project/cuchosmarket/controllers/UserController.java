package com.project.cuchosmarket.controllers;

import com.project.cuchosmarket.dto.DtCustomer;
import com.project.cuchosmarket.dto.DtResponse;
import com.project.cuchosmarket.dto.DtUser;
import com.project.cuchosmarket.exceptions.MarketBranchNotExistException;
import com.project.cuchosmarket.exceptions.UserExistException;
import com.project.cuchosmarket.exceptions.UserNotExistException;
import com.project.cuchosmarket.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

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

    @PostMapping("employees/market_branches/{branch_id}")
    public DtResponse addEmployee(@PathVariable("branch_id") Long branch_id, @RequestBody DtUser employee) {
        try {
            userService.addEmployee(branch_id, employee);
        } catch (MarketBranchNotExistException | UserExistException | IllegalArgumentException e) {
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

    @PostMapping("/customers")
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

    @GetMapping("/user-list")
    public DtResponse getUsers() {
        return DtResponse.builder()
                .error(false)
                .message(String.valueOf(userService.getUsers().size()))
                .data(userService.getUsers())
                .build();
    }

    @DeleteMapping("/employees/{employee_id}")
    public DtResponse deleteEmployee(@PathVariable("employee_id") Long employee_id) {
        userService.deleteEmployee(employee_id);
        return DtResponse.builder()
                .error(false)
                .message("Empleado borrado con exito.")
                .build();
    }
}
