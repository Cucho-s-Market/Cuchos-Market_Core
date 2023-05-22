package com.project.cuchosmarket.controllers;

import com.project.cuchosmarket.dto.DtResponse;
import com.project.cuchosmarket.services.MarketBranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/marketBranches")
public class MarketBranchController {
    private final MarketBranchService marketBranchService;

    @GetMapping
    public DtResponse listMarketBranches() {
        return DtResponse.builder()
                .error(false)
                .data(marketBranchService.getMarketBranches())
                .build();
    }
}
