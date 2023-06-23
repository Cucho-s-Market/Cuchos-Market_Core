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

    public List<DtStatistics.DtTopProduct> getTopSellingProducts(Long branch_id) {
        LocalDate currentDate = LocalDate.now();

        LocalDate startDate = LocalDate.of(currentDate.getYear(), currentDate.getMonthValue(), 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        return (branch_id == null) ?
                orderRepository.findTopSellingProducts(startDate, endDate) :
                branchRepository.findTopSellingProductsByBranch(branch_id, startDate, endDate);
    }
}
