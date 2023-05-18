package com.project.cuchosmarket.security;

import com.project.cuchosmarket.exceptions.UserNotExistException;
import com.project.cuchosmarket.models.User;
import com.project.cuchosmarket.repositories.UserRepository;
import lombok.SneakyThrows;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private UserRepository userRepository;

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotExistException("El usuario con el email " + email + " no existe");
        }

        return new UserDetailsImpl(user);
    }
}
