package com.project.cuchosmarket.controllers;

import com.project.cuchosmarket.dto.DtResponse;
import com.project.cuchosmarket.services.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RequiredArgsConstructor
@RestController
@RequestMapping("/statistics")
public class StatisticsController {
    private final StatisticsService statisticsService;

    @GetMapping("/products")
    public DtResponse getTopSellingProducts(@RequestParam(value = "branch_id", required = false) Long branch_id,
                                            @RequestParam(value = "startDate") LocalDate startDate,
                                            @RequestParam(value = "endDate") LocalDate endDate) {
        return DtResponse.builder()
                .error(false)
                .data(statisticsService.getTopSellingProducts(branch_id, startDate, endDate))
                .build();
    }

    @GetMapping("/sales")
    public DtResponse getSales(@RequestParam(value = "branch_id", required = false) Long branch_id,
                               @RequestParam(value = "startDate") LocalDate startDate,
                               @RequestParam(value = "endDate") LocalDate endDate) {
        return DtResponse.builder()
                .error(false)
                .data((branch_id == null) ?
                        statisticsService.getSalesByBranch(startDate, endDate):
                        statisticsService.getSalesInBranch(branch_id, startDate, endDate))
                .build();
    }

    @GetMapping("/profit")
    public DtResponse getProfit(@RequestParam(value = "branch_id", required = false) Long branch_id,
                                @RequestParam(value = "startDate") LocalDate startDate,
                                @RequestParam(value = "endDate") LocalDate endDate) {
        return DtResponse.builder()
                .error(false)
                .data((branch_id == null) ?
                        statisticsService.getProfitByBranch(startDate, endDate):
                        statisticsService.getProfitInBranch(branch_id, startDate, endDate))
                .build();
    }

    @GetMapping("/brands")
    public DtResponse getTopBrands(@RequestParam(value = "branch_id", required = false) Long branch_id,
                                   @RequestParam(value = "startDate") LocalDate startDate,
                                   @RequestParam(value = "endDate") LocalDate endDate) {
        return DtResponse.builder()
                .error(false)
                .data(statisticsService.getTopBrands(branch_id, startDate, endDate))
                .build();
    }

    @GetMapping("/promotions")
    public DtResponse getTopPromotions(@RequestParam(value = "branch_id", required = false) Long branch_id,
                                       @RequestParam(value = "startDate") LocalDate startDate,
                                       @RequestParam(value = "endDate") LocalDate endDate) {
        return DtResponse.builder()
                .error(false)
                .data(statisticsService.getTopPromotions(branch_id, startDate, endDate))
                .build();
    }

}
