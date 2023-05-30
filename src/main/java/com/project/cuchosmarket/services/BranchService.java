package com.project.cuchosmarket.services;

import com.project.cuchosmarket.dto.DtMarketBranch;
import com.project.cuchosmarket.repositories.MarketBranchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BranchService {
    private final MarketBranchRepository marketBranchRepository;

    public List<DtMarketBranch> getMarketBranches() {
        return marketBranchRepository.findAllSelected();
    }
}
