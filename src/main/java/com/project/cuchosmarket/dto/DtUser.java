package com.project.cuchosmarket.dto;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DtUser {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String role;

    public DtUser(Long id, String firstName, String lastName, String email, String password) {
    }
}

