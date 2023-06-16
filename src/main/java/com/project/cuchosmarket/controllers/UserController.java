package com.project.cuchosmarket.controllers;

import com.project.cuchosmarket.dto.DtAddress;
import com.project.cuchosmarket.dto.DtCustomer;
import com.project.cuchosmarket.dto.DtResponse;
import com.project.cuchosmarket.dto.DtUser;
import com.project.cuchosmarket.exceptions.*;
import com.project.cuchosmarket.services.AddressService;
import com.project.cuchosmarket.services.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final AddressService addressService;

    @GetMapping
    public DtResponse getUsers() {
        return DtResponse.builder()
                .error(false)
                .message(String.valueOf(userService.getUsers().size()))
                .data(userService.getUsers())
                .build();
    }

    @PostMapping("/auth/login")
    public DtResponse login(@RequestBody DtUser user) {
        DtResponse token;
        try {
            token = userService.authenticate(user);
        } catch (UserNotExistException | CustomerDisabledException e) {
            return DtResponse.builder()
                    .error(true)
                    .message(e.getMessage())
                    .build();
        }

        token.setError(false);
        return token;
    }

    @PostMapping("/auth/resetPassword")
    public DtResponse resetPassword(HttpServletRequest request, @RequestBody DtUser user) {
        try {
            userService.resetPassword(request, user);
        } catch (UserNotExistException | CustomerDisabledException e) {
            return DtResponse.builder()
                    .error(true)
                    .message(e.getMessage())
                    .build();
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return DtResponse.builder()
                .error(false)
                .message("e.getMessage()")
                .build();
    }

    @PostMapping("/auth/updatePassword")
    public DtResponse updatePassword(@RequestBody DtUser dtUser) {
        DtResponse token;
        try {
            String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            userService.updatePassword(userEmail, dtUser);
            token = userService.authenticate(dtUser);
        } catch (IllegalArgumentException |UserNotExistException | CustomerDisabledException e) {
            return DtResponse.builder()
                    .error(true)
                    .message(e.getMessage())
                    .build();
        }

        token.setError(false);
        return token;
    }

    //Este endpoint tambien se podria saltear y hacer que el link del email redirija a un formulario para cambiar las pass en Front
    //Y endpoint de actualizar la password verificaria el token y en caso de que este bien actualiza la contrs o tirara un 403 Forbidden
    @GetMapping("/changePassword")
    public String showChangePasswordPage(Locale locale, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = (String) authentication.getCredentials();

        model.addAttribute("token", token);
        return "redirect:/updatePassword.html?lang=" + locale.getLanguage(); //Checkear el redireccionamiento en front
        //La idea es que el link del email pegue a este endpoint para validar el token y hacer que se redireccione a la pagina de actualizar la password
    }


    @PostMapping("/{branch_id}/employee")
    public DtResponse addEmployee(@PathVariable("branch_id") Long branch_id, @RequestBody DtUser employee) {
        try {
            userService.addEmployee(branch_id, employee);
        } catch (BranchNotExistException | UserExistException | IllegalArgumentException e) {
            return DtResponse.builder()
                    .error(true)
                    .message(e.getMessage())
                    .build();
        }

        return DtResponse.builder()
                .error(false)
                .message("Usuario añadido con exito.")
                .build();
    }

    @PostMapping("/customer")
    public DtResponse addCustomer(@RequestBody DtCustomer customer) {
        DtResponse response;
        try {
            response = userService.addCustomer(customer);
        } catch (UserExistException | IllegalArgumentException e) {
            return DtResponse.builder()
                    .error(true)
                    .message(e.getMessage())
                    .build();

        }

        response.setError(false);
        response.setMessage("Cliente añadido con exito.");
        return response;
    }

    @PutMapping
    public DtResponse updateUser(@RequestBody DtCustomer dtUser) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            userService.updateUser(userEmail, dtUser);
        } catch (UserNotExistException | IllegalArgumentException | UserExistException e) {
            return DtResponse.builder()
                    .error(true)
                    .message(e.getMessage())
                    .build();
        }

        return DtResponse.builder()
                .error(false)
                .message("Usuario actualizado.")
                .build();
    }

    @DeleteMapping("/{user_id}")
    public DtResponse deleteUser(@PathVariable("user_id") Long user_id) {
        try {
            userService.deleteUser(user_id);
        } catch (UserNotExistException e) {
            return DtResponse.builder()
                    .error(true)
                    .message(e.getMessage())
                    .build();
        }
        return DtResponse.builder()
                .error(false)
                .message("Usuario eliminado con exito.")
                .build();
    }

    @GetMapping("/customer/address")
    public DtResponse getAddress() {

        try {
            String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            return DtResponse.builder()
                    .error(false)
                    .message(String.valueOf(addressService.getAddress(userEmail).size()))
                    .data(addressService.getAddress(userEmail))
                    .build();

        } catch (UserNotExistException e) {
            return DtResponse.builder()
                    .error(true)
                    .message(e.getMessage())
                    .build();
        }

    }

    @PostMapping("/customer/address")
    public DtResponse addAddress(@RequestBody DtAddress address) {
        try {
            String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            addressService.addAddress(address, userEmail);
        } catch (UserNotExistException | InvalidAddressException e) {
            return DtResponse.builder()
                    .error(true)
                    .message(e.getMessage())
                    .build();
        }

        return DtResponse.builder()
                .error(false)
                .message("Direccion añadida con exito.")
                .build();
    }

    @PutMapping("/customer/address")
    public DtResponse updateAddress(@RequestBody DtAddress address) {
        try {
            String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            addressService.updateAddress(userEmail, address);
        } catch (UserNotExistException | AddressNotExistException | InvalidAddressException e) {
            return DtResponse.builder()
                    .error(true)
                    .message(e.getMessage())
                    .build();
        }

        return DtResponse.builder()
                .error(false)
                .message("Direccion actualizada con exito.")
                .build();
    }

    @DeleteMapping("/customer/address/{address_id}")
    public DtResponse deleteAddress(@PathVariable("address_id") Long address_id) {
        try {
            String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            addressService.deleteAddress(userEmail, address_id);
        } catch (UserNotExistException | AddressNotExistException e) {
            return DtResponse.builder()
                    .error(true)
                    .message(e.getMessage())
                    .build();
        }

        return DtResponse.builder()
                .error(false)
                .message("Direccion eliminada con exito.")
                .build();
    }

    @PutMapping("/admin/disable-customer")
    public DtResponse disableCustomer(@RequestBody DtCustomer customer) {
        try {
            userService.disableCustomer(customer);
        } catch (UserNotExistException e) {
            return DtResponse.builder()
                    .error(true)
                    .message(e.getMessage())
                    .build();
        }

        return DtResponse.builder()
                .error(false)
                .message("Usuario " + (customer.isDisabled() ? "bloqueado" : "desbloqueado") + " con éxito")
                .build();
    }
}
