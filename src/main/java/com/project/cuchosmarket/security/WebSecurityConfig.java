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

import static com.project.cuchosmarket.enums.Role.*;

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

                //All users
                .requestMatchers(
                        HttpMethod.POST,
                        "/users/auth/**",
                        "/users/add-customer"
                ).permitAll()

                .requestMatchers(
                        HttpMethod.GET,
                        "/categories/get-categories",
                        "/branches/get-branches",
                        "/products"
                ).permitAll()

                //Admin users
                .requestMatchers(
                        "/branches/admin/**",
                        "/categories/admin/**",
                        "/orders/admin/**",
                        "/products/admin/**",
                        "/users/admin/**"
                ).hasRole(ADMIN.name())

                //Employees users
                .requestMatchers(
                        "/products/employee/**",
                        "/orders/employee/**"
                ).hasRole(EMPLOYEE.name())

                //Customers users
                .requestMatchers(
                        HttpMethod.POST,
                        "/orders/**"
                ).hasRole(CUSTOMER.name())

                //Admin & Customers users
                .requestMatchers(
                        HttpMethod.DELETE,
                        "/users/delete-user/**"
                ).hasAnyRole(ADMIN.name(), CUSTOMER.name())

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
