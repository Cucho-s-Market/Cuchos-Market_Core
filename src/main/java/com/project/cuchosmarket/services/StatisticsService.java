package com.project.cuchosmarket.services;

import com.project.cuchosmarket.dto.DtStatistics;
import com.project.cuchosmarket.repositories.BranchRepository;
import com.project.cuchosmarket.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Service
public class StatisticsService {
    private final OrderRepository orderRepository;
    private final BranchRepository branchRepository;

    public List<DtStatistics.DtTopProduct> getTopSellingProducts(Long branch_id, LocalDate startDate, LocalDate endDate) {
        return (branch_id == null) ?
                orderRepository.findTopSellingProducts(startDate, endDate) :
                branchRepository.findTopSellingProductsByBranch(branch_id, startDate, endDate);
    }

    public List<DtStatistics.DtSalesByBranch> getSalesByBranch(LocalDate startDate, LocalDate endDate) {
        return branchRepository.findSales(startDate, endDate);
    }

    public List<DtStatistics.DtSalesInBranch> getSalesInBranch(Long branchId, LocalDate startDate, LocalDate endDate) {
        return branchRepository.findSalesInBranch(branchId, startDate, endDate);
    }

    public List<DtStatistics.DtProfitByBranch> getProfitByBranch(LocalDate startDate, LocalDate endDate) {
        return branchRepository.calculateProfitByBranch(startDate, endDate);
    }

    public List<DtStatistics.DtProfitInBranch> getProfitInBranch(Long branchId, LocalDate startDate, LocalDate endDate) {
        return branchRepository.calculateProfitInBranch(branchId, startDate, endDate);
    }

    public List<DtStatistics.DtPopularBrand> getTopBrands(Long branch_id, LocalDate startDate, LocalDate endDate) {
        return (branch_id == null) ?
                orderRepository.findTopBrands(startDate, endDate) :
                branchRepository.findTopBrandsByBranch(branch_id, startDate, endDate);
    }

    public List<DtStatistics.DtSuccessfulPromotion> getTopPromotions(Long branch_id, LocalDate startDate, LocalDate endDate) {
        return (branch_id == null) ?
                orderRepository.findTopPromotions(startDate, endDate) :
                branchRepository.findTopPromotionsByBranch(branch_id, startDate, endDate);
    }
}
