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
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    private final EmailService emailService;

    public User validateUserAuthentication(DtUser dtUser) throws UserNotExistException, CustomerDisabledException {
        User user = userRepository.findByEmail(dtUser.getEmail());
        if (user == null) throw new UserNotExistException();

        if (user.getRole().equals(Role.CUSTOMER)) {
            if (customerRepository.findById(user.getId()).get().getDisabled()) throw new CustomerDisabledException("Usuario bloqueado.");
        }
        return user;
    }

    public DtResponse authenticate(DtUser dtUser) throws UserNotExistException, CustomerDisabledException {
        User user = validateUserAuthentication(dtUser);

        if (user.getRole().equals(Role.EMPLOYEE)) {
            Employee employee = (Employee) user;
            employee.getBranch().setOrders(null);
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

    public void resetPassword(DtUser dtUser) throws UserNotExistException, CustomerDisabledException, MessagingException {
        User user = validateUserAuthentication(dtUser);
        var jwtToken = jwtService.createToken(new HashMap<>(), new UserDetailsImpl(user));
        emailService.sendResetTokenEmail(jwtToken, user);
    }

    public void updatePassword(DtUser dtUser) throws UserNotExistException, CustomerDisabledException {
        User user = validateUserAuthentication(dtUser);

        if (StringUtils.isBlank(dtUser.getPassword())) throw new IllegalArgumentException("La contraseña no puede estar vacia.");
        else if (passwordEncoder.matches(dtUser.getPassword(), user.getPassword())) throw new IllegalArgumentException("La nueva contraseña ha de ser distinta a la anterior.");

        user.setPassword(passwordEncoder.encode(dtUser.getPassword()));
        userRepository.save(user);
    }

    private void validateUser(DtUser dtUser) {

        if (StringUtils.isBlank(dtUser.getPassword())) throw new IllegalArgumentException("La contraseña no puede estar vacia.");

        if (StringUtils.isBlank(dtUser.getFirstName()) || StringUtils.isBlank(dtUser.getLastName()) || dtUser.getFirstName().length() > 25 || dtUser.getLastName().length() > 25 )
            throw new IllegalArgumentException("Datos invalidos: Nombre invalido.");
    }

    private void validateCustomer(Customer customer, DtCustomer dtCustomer) throws UserExistException {
        if((customer == null || customer.getDni() != dtCustomer.getDni()) && customerRepository.existsByDni(dtCustomer.getDni())) {
            throw new UserExistException("Usuario con CI " + dtCustomer.getDni() + " ya existe en el sistema.");
        }
        if(String.valueOf(dtCustomer.getTelephone()).length() < 8) throw new IllegalArgumentException("Datos invalidos: Telefono invalido.");
        if(dtCustomer.getBirthdate() != null && dtCustomer.getBirthdate().isAfter(LocalDate.now())) throw new IllegalArgumentException("Datos invalidos: Fecha de nacimiento invalida.");
    }

    public void addEmployee(Long branchId, DtUser user) throws BranchNotExistException, UserExistException {
        if((userRepository.existsByEmail(user.getEmail()))) throw new UserExistException("Usuario con email " + user.getEmail() + " ya se encuentra en el sistema.");

        Branch branch = branchRepository.findById(branchId).orElseThrow(() -> new BranchNotExistException(branchId));

        validateUser(user);

        Employee employee = new Employee(user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                passwordEncoder.encode(user.getPassword()),
                branch);
        employeeRepository.save(employee);
    }

    public DtResponse addCustomer(DtCustomer dtCustomer) throws UserExistException {

        if((userRepository.existsByEmail(dtCustomer.getEmail()))) throw new UserExistException("Usuario con email " + dtCustomer.getEmail() + " ya se encuentra en el sistema.");

        validateUser(dtCustomer);
        validateCustomer(null, dtCustomer);

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

    @Transactional
    public void updateUser(DtCustomer dtUser) throws UserNotExistException, UserExistException {
        User user = userRepository.findByEmail(dtUser.getEmail());
        if (user == null) throw new UserNotExistException();

        if (user.getRole().equals(Role.CUSTOMER)) {
            Customer customer = customerRepository.findById(user.getId()).orElseThrow(UserNotExistException::new);

            validateCustomer(customer, dtUser);
            customer.setBirthdate(dtUser.getBirthdate());
            customer.setDni(dtUser.getDni());
            customer.setTelephone(dtUser.getTelephone());

            customerRepository.save(customer);
        }

        if (StringUtils.isNotBlank(dtUser.getEmail())) {
            if (!user.getEmail().equals(dtUser.getEmail())) {
                User newUser = userRepository.findByEmail(dtUser.getEmail());
                if (newUser != null) throw new UserExistException("Usuario con email " + dtUser.getEmail() + " ya existe.");
                else user.setEmail(dtUser.getEmail());
            }
        }

        if (StringUtils.isNotBlank(dtUser.getFirstName())) {
            if (dtUser.getFirstName().length() > 25) throw new IllegalArgumentException("Datos invalidos: Nombre invalido.");
            user.setFirstName(dtUser.getFirstName());
        }

        if (StringUtils.isNotBlank(dtUser.getLastName())) {
            if (dtUser.getLastName().length() > 25) throw new IllegalArgumentException("Datos invalidos: Nombre invalido.");
            user.setLastName(dtUser.getLastName());
        }

        if (StringUtils.isNotBlank(dtUser.getPassword())) user.setPassword(passwordEncoder.encode(dtUser.getPassword()));

        userRepository.save(user);
    }

    public List<DtUser> getUsers() {
        List<DtUser> dtUsers = new ArrayList<>();
        List<User> users = userRepository.findAll();

        for (User user : users) {
            if (user.getRole().equals(Role.CUSTOMER)) {
                Customer customer = (Customer) user;

                dtUsers.add(new DtCustomer(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(),
                        null, user.getRole().name(), customer.getDisabled()));

            } else dtUsers.add(new DtUser(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(),
                    null, user.getRole().name()));
        }

        return dtUsers;
    }

    public User getUser(Long userId) throws UserNotExistException {
        User user = userRepository.findById(userId).orElseThrow(UserNotExistException::new);

        switch (user.getRole()) {
            case CUSTOMER:
                return customerRepository.findById(userId).get();
            case EMPLOYEE:
                Employee employee = employeeRepository.findById(userId).get();
                employee.getBranch().setOrders(null);
                employee.getBranch().setIssues(null);
                return employee;
            case ADMIN:
                return user;
            default:
                return null;
        }
    }

    public DtResponse addAdmin(DtUser admin) throws UserExistException {

        if((userRepository.existsByEmail(admin.getEmail()))) throw new UserExistException("Usuario con email " + admin.getEmail() + " ya se encuentra en el sistema.");

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
        User user = userRepository.findById(id).orElseThrow(UserNotExistException::new);

        if (user.getRole().equals(Role.EMPLOYEE)) {
            employeeRepository.deleteById(id);
        } else if (user.getRole().equals(Role.CUSTOMER)) {
            customerRepository.deleteById(id);
        }

        userRepository.delete(user);
    }

    public void disableCustomer(DtCustomer dtCustomer) throws UserNotExistException {
        User user = userRepository.findByEmail(dtCustomer.getEmail());
        if (user == null) throw new UserNotExistException();

        Customer customer = customerRepository.findById(user.getId()).orElseThrow(UserNotExistException::new);;
        customer.setDisabled(dtCustomer.isDisabled());

        customerRepository.save(customer);
    }
}
