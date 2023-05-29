package com.project.cuchosmarket.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DtAddress {
    private Long id;
    private String address;
    private int doorNumber;
    private String location;
    private String state;
}

