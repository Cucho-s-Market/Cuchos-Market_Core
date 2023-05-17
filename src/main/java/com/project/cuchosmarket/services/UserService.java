package com.project.cuchosmarket.services;

import com.project.cuchosmarket.dto.DtUser;
import com.project.cuchosmarket.exceptions.MarketBranchNotExist;
import com.project.cuchosmarket.exceptions.UserExistException;
import com.project.cuchosmarket.models.Employee;
import com.project.cuchosmarket.models.MarketBranch;
import com.project.cuchosmarket.repositories.EmployeeRepository;
import com.project.cuchosmarket.repositories.MarketBranchRepository;
import com.project.cuchosmarket.repositories.UserRepository;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final MarketBranchRepository marketBranchRepository;

    private void validateUser(DtUser user) throws UserExistException {
        if(userRepository.existsByEmail(user.getEmail())) {
            throw new UserExistException("Usuario con email " + user.getEmail() + " ya se encuentra en el sistema");
        }

        if (StringUtils.isBlank(user.getPassword())) {
            throw new IllegalArgumentException("La contraseña no puede estar vacia");
        }

        if (StringUtils.isBlank(user.getFirstName()) || StringUtils.isBlank(user.getLastName()) ||
                user.getFirstName().length() > 25 || user.getLastName().length() > 25 ) {
            throw new IllegalArgumentException("Datos invalidos");
        }
    }

    public void addEmployee(Long branchId, DtUser user) throws MarketBranchNotExist, UserExistException {
        Optional<MarketBranch> marketBranch = marketBranchRepository.findById(branchId);

        if (marketBranch.isEmpty()) {
            throw new MarketBranchNotExist("La sucursal con la id " + branchId + " no existe");
        }

        validateUser(user);

        Employee employee = new Employee(user.getFirstName(), user.getLastName(), user.getEmail(),
                user.getPassword(), user.getPassword(), marketBranch.get());
        employeeRepository.save(employee);
    }
}
