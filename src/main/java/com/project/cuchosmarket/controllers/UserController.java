package com.project.cuchosmarket.controllers;

import com.project.cuchosmarket.dto.DtResponse;
import com.project.cuchosmarket.dto.DtUser;
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

    @PostMapping("/user-list")
    public DtResponse getUsers() {
        return DtResponse.builder()
                .error(false)
                .message(String.valueOf(userService.getUsers().size()))
                .data(userService.getUsers())
                .build();
    }
}
