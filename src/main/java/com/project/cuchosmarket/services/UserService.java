package com.project.cuchosmarket.services;

import com.project.cuchosmarket.dto.DtUser;
import com.project.cuchosmarket.models.Employee;
import com.project.cuchosmarket.models.MarketBranch;
import com.project.cuchosmarket.repositories.EmployeeRepository;
import com.project.cuchosmarket.repositories.MarketBranchRepository;
import com.project.cuchosmarket.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final MarketBranchRepository marketBranchRepository;

    public void addEmployee(Long branchId, DtUser user) {
        Optional<MarketBranch> marketBranch = marketBranchRepository.findById(branchId);
        if (marketBranch == null) {
            //tira excepcion
        }

        if(user.getEmail() == null || userRepository.existsByEmail(user.getEmail())) {
            //excepcion
        }

        Employee employee = new Employee(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword(), user.getPassword(), marketBranch.get());
        employeeRepository.save(employee);
    }
}
