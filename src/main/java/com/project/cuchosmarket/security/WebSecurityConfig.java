package com.project.cuchosmarket.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.project.cuchosmarket.enums.Role.ADMIN;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class WebSecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf()
                .disable()
                .authorizeHttpRequests()

                .requestMatchers(
                        "/v3/api-docs/**",
                        "/swagger-resources/**",
                        "/doc/swagger-ui/**"
                )
                    .permitAll()

                .requestMatchers(HttpMethod.POST, "/users/auth/**", "users/customers")
                    .permitAll()

                .requestMatchers(HttpMethod.GET, "/users/**", "/category/**", "/marketBranches", "/products")
                    .permitAll()

                .requestMatchers(HttpMethod.POST, "/users/employees/**", "/product/**").hasRole(ADMIN.name())
                .requestMatchers(HttpMethod.GET, "/users/user-list").hasRole(ADMIN.name())
                .requestMatchers(HttpMethod.DELETE, "/users/employees/**").hasRole(ADMIN.name())

                .anyRequest()
                    .authenticated()
                .and()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}
