package com.project.cuchosmarket.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DtCustomer extends DtUser{

    private LocalDate birthdate;
    private long telephone;
    private long dni;
    private boolean disabled;


    public DtCustomer(Long id, String firstName, String lastName, String email, String password, String role) {
        super(id, firstName, lastName, email, password, role);
    }
}
