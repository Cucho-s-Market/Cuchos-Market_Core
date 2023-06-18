package com.project.cuchosmarket.controllers;

import com.project.cuchosmarket.dto.DtResponse;
import com.project.cuchosmarket.exceptions.EmployeeNotWorksInException;
import com.project.cuchosmarket.services.BranchService;
import com.project.cuchosmarket.services.IssueService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/branches")
public class BranchController {
    private final BranchService branchService;
    private final IssueService issueService;

    @GetMapping
    public DtResponse listMarketBranches() {
        return DtResponse.builder()
                .error(false)
                .data(branchService.getMarketBranches())
                .build();
    }

    @GetMapping("/{branch_id}/issues")
    public DtResponse listOrderIssues(@PathVariable("branch_id") Long branch_id,
                                      @RequestParam(value = "page_number", required = false, defaultValue = "0") int page_number,
                                      @RequestParam(value = "page_size", required = false, defaultValue = "50") int page_size,
                                      @RequestParam(value = "orderDirection", required = false) String orderDirection) {
        try {
            String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

            return DtResponse.builder()
                    .error(false)
                    .data(issueService.getIssuesByBranch(userEmail, branch_id, orderDirection, page_number, page_size))
                    .build();
        } catch (EmployeeNotWorksInException e) {
            return DtResponse.builder()
                    .error(true)
                    .message(e.getMessage())
                    .build();
        }
    }
}
