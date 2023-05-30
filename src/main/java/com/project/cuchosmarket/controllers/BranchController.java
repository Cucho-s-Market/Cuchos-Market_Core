package com.project.cuchosmarket.controllers;

import com.project.cuchosmarket.dto.DtResponse;
import com.project.cuchosmarket.services.BranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/branches")
public class BranchController {
    private final BranchService branchService;

    @GetMapping("/get-branches")
    public DtResponse listMarketBranches() {
        return DtResponse.builder()
                .error(false)
                .data(branchService.getMarketBranches())
                .build();
    }
}
