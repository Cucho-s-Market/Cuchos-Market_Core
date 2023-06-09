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
                .cors()
                .and()
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
                        "/users/customer"
                ).permitAll()

                .requestMatchers(
                        HttpMethod.GET,
                        "/categories",
                        "/branches",
                        "/products/**",
                        "/promotions"
                ).permitAll()

                //Admin users
                .requestMatchers(
                        HttpMethod.POST,
                        "/products",
                        "/categories",
                        "/users/{branch_id}/employee"
                ).hasRole(ADMIN.name())

                .requestMatchers(
                        HttpMethod.PUT,
                        "/products"
                ).hasRole(ADMIN.name())

                .requestMatchers(
                        HttpMethod.DELETE,
                        "/products"
                ).hasRole(ADMIN.name())

                .requestMatchers(
                        HttpMethod.GET,
                        "/users"
                ).hasRole(ADMIN.name())

                .requestMatchers(
                        "/users/admin/**",
                        "/branches/admin/**",
                        "/categories/admin/**",
                        "/orders/admin/**"
                ).hasRole(ADMIN.name())

                //Employees users
                .requestMatchers(
                        HttpMethod.GET,
                        "/orders/branch/**",
                        "/products/{branch_id}/{category_id}/stock"
                ).hasRole(EMPLOYEE.name())

                .requestMatchers(
                        HttpMethod.PUT,
                        "/orders/employee",
                        "/promotions"
                ).hasRole(EMPLOYEE.name())

                .requestMatchers(
                        HttpMethod.POST,
                        "/promotions"
                ).hasRole(EMPLOYEE.name())

                //Customers users
                .requestMatchers(
                        HttpMethod.POST,
                        "/users/customer/**",
                        "/orders/**"
                ).hasRole(CUSTOMER.name())

                .requestMatchers(
                        HttpMethod.GET,
                        "/orders/customer"
                ).hasRole(CUSTOMER.name())

                .requestMatchers(
                        HttpMethod.PUT,
                        "/users/customer/**",
                        "/orders/{order_id}"
                ).hasRole(CUSTOMER.name())

                .requestMatchers(
                        HttpMethod.DELETE,
                        "/users/customer/**"
                ).hasRole(CUSTOMER.name())

                //Admin & Customers users
                .requestMatchers(
                        HttpMethod.DELETE,
                        "/users/**"
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
