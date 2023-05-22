package com.project.cuchosmarket.services;

import com.project.cuchosmarket.dto.DtResponse;
import com.project.cuchosmarket.dto.DtCustomer;
import com.project.cuchosmarket.dto.DtUser;
import com.project.cuchosmarket.exceptions.MarketBranchNotExist;
import com.project.cuchosmarket.exceptions.UserExistException;
import com.project.cuchosmarket.exceptions.UserNotExistException;
import com.project.cuchosmarket.models.Customer;
import com.project.cuchosmarket.models.Employee;
import com.project.cuchosmarket.models.MarketBranch;
import com.project.cuchosmarket.models.User;
import com.project.cuchosmarket.models.User;
import com.project.cuchosmarket.repositories.CustomerRepository;
import com.project.cuchosmarket.repositories.EmployeeRepository;
import com.project.cuchosmarket.repositories.MarketBranchRepository;
import com.project.cuchosmarket.repositories.UserRepository;
import com.project.cuchosmarket.security.JwtService;
import com.project.cuchosmarket.security.UserDetailsImpl;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final CustomerRepository customerRepository;
    private final MarketBranchRepository marketBranchRepository;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public DtResponse authenticate(DtUser dtUser) throws UserNotExistException {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dtUser.getEmail(),
                        dtUser.getPassword()
                )
        );
        User user = userRepository.findByEmail(dtUser.getEmail());
        if (user == null) {
            throw new UserNotExistException("Usuario no existe.");
        }

        var jwtToken = jwtService.createToken(new HashMap<>(), new UserDetailsImpl(user));

        return DtResponse.builder()
                .token(jwtToken)
                .build();
    }

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

        Employee employee = new Employee(user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPassword(),
                passwordEncoder.encode(user.getPassword()),
                marketBranch.get());
        employeeRepository.save(employee);
    }

    public void addCustomer( DtCustomer dtCustomer) throws UserExistException {

        if(customerRepository.existsByDni(dtCustomer.getDni())) {
            throw new UserExistException("ya se encuentra en el sistema");
        }

        validateUser(dtCustomer);

        Customer customer = new Customer(dtCustomer.getFirstName(),dtCustomer.getLastName(),dtCustomer.getEmail(),dtCustomer.getPassword(),
                    dtCustomer.getPassword(),dtCustomer.getBirthdate(),dtCustomer.getTelephone(),dtCustomer.getDni());

        customerRepository.save(customer);
    }

    public List<DtUser> getUsers() {
        List<DtUser> dtUsers = new ArrayList<>();
        List<User> users = userRepository.findAll();

        users.forEach(user -> dtUsers.add(new DtUser(user.getFirstName(), user.getLastName(), user.getEmail(), null)));

        return dtUsers;
    }
}
