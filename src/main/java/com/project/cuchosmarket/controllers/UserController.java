package com.project.cuchosmarket.controllers;

import com.project.cuchosmarket.dto.DtUser;
import com.project.cuchosmarket.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/market_branch/{branch_id}/employee")
    public ResponseEntity addEmployee(@PathVariable("branch_id") Long branch_id, @RequestBody DtUser employee) {

        userService.addEmployee(branch_id, employee);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
