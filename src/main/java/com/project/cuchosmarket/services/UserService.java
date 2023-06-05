package com.project.cuchosmarket.services;

import com.project.cuchosmarket.dto.DtCustomer;
import com.project.cuchosmarket.dto.DtResponse;
import com.project.cuchosmarket.dto.DtUser;
import com.project.cuchosmarket.enums.Role;
import com.project.cuchosmarket.exceptions.BranchNotExistException;
import com.project.cuchosmarket.exceptions.CustomerDisabledException;
import com.project.cuchosmarket.exceptions.UserExistException;
import com.project.cuchosmarket.exceptions.UserNotExistException;
import com.project.cuchosmarket.models.*;
import com.project.cuchosmarket.repositories.*;
import com.project.cuchosmarket.security.JwtService;
import com.project.cuchosmarket.security.UserDetailsImpl;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final AdminRepository adminRepository;
    private final CustomerRepository customerRepository;
    private final BranchRepository branchRepository;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public DtResponse authenticate(DtUser dtUser) throws UserNotExistException, CustomerDisabledException {
        User user = userRepository.findByEmail(dtUser.getEmail());
        if (user == null) {
            throw new UserNotExistException();
        }

        if (user.getRole().equals(Role.CUSTOMER)) {
            if (customerRepository.findById(user.getId()).get().isDisabled()) {
                throw new CustomerDisabledException("Usuario inhabilitado.");
            }
            Customer customer = (Customer) user;
            customer.setOrdersPlaced(null);
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dtUser.getEmail(),
                        dtUser.getPassword()
                )
        );

        var jwtToken = jwtService.createToken(new HashMap<>(), new UserDetailsImpl(user));
        user.setPassword(null);

        return DtResponse.builder()
                .data(user)
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

    public void addEmployee(Long branchId, DtUser user) throws BranchNotExistException, UserExistException {
        Branch marketBranch = branchRepository.findById(branchId).orElseThrow(() -> new BranchNotExistException(branchId));
        validateUser(user);

        Employee employee = new Employee(user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                passwordEncoder.encode(user.getPassword()),
                marketBranch);
        employeeRepository.save(employee);
    }

    public DtResponse addCustomer(DtCustomer dtCustomer) throws UserExistException {
        validateUser(dtCustomer);
        if(customerRepository.existsByDni(dtCustomer.getDni())) throw new UserExistException("Usuario con CI " + dtCustomer.getDni() + " ya existe en el sistema.");

        Customer customer = new Customer(dtCustomer.getFirstName(),
                dtCustomer.getLastName(),
                dtCustomer.getEmail(),
                passwordEncoder.encode(dtCustomer.getPassword()),
                dtCustomer.getBirthdate(),
                dtCustomer.getTelephone(),
                dtCustomer.getDni());

        customerRepository.save(customer);
        var jwtToken = jwtService.createToken(new HashMap<>(), new UserDetailsImpl(customer));

        return DtResponse.builder()
                .token(jwtToken)
                .build();
    }

    public List<DtUser> getUsers() {
        List<DtUser> dtUsers = new ArrayList<>();
        List<User> users = userRepository.findAll();

        users.forEach(user -> dtUsers.add(new DtUser(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), null, user.getRole().name())));

        return dtUsers;
    }

    public DtResponse addAdmin(DtUser admin) throws UserExistException {
        validateUser(admin);
        Admin administrator = new Admin(admin.getFirstName(),
                admin.getLastName(),
                admin.getEmail(),
                passwordEncoder.encode(admin.getPassword()));
        adminRepository.save(administrator);

        var jwtToken = jwtService.createToken(new HashMap<>(), new UserDetailsImpl(administrator));

        return DtResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Transactional
    public void deleteUser(Long id) throws UserNotExistException {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new UserNotExistException();
        }

        if (user.get().getRole().equals(Role.EMPLOYEE)) {
            employeeRepository.deleteById(id);
        } else if (user.get().getRole().equals(Role.CUSTOMER)) {
            customerRepository.deleteById(id);
        }

        userRepository.delete(user.get());
    }

    public void disableCustomer(DtCustomer dtCustomer) throws UserNotExistException {
        User user = userRepository.findByEmail(dtCustomer.getEmail());
        if (user == null) throw new UserNotExistException();

        Customer customer = customerRepository.findById(user.getId()).get();
        customer.setDisabled(dtCustomer.isDisabled());

        customerRepository.save(customer);
    }
}
