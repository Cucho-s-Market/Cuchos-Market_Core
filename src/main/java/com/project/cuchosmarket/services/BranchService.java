package com.project.cuchosmarket.services;

import com.project.cuchosmarket.dto.DtBranch;
import com.project.cuchosmarket.repositories.BranchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BranchService {
    private final BranchRepository branchRepository;

    public List<DtBranch> getMarketBranches() {
        return branchRepository.findAllSelected();
    }
}
