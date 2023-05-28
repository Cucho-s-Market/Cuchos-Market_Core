package com.project.cuchosmarket.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class DtUser {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String role;
}
