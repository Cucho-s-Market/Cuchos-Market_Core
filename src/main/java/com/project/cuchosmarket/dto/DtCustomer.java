package com.project.cuchosmarket.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter

public class DtCustomer extends DtUser{

    private LocalDate birthdate;
    private int telephone;
    private int dni;
    private boolean disabled;


    public DtCustomer(String firstName, String lastName, String email, String password) {
        super(firstName, lastName, email, password);
    }
}
