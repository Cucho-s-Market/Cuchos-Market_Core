package com.project.cuchosmarket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class DtAddress {
    private Long id;
    private String address;
    private int doorNumber;
    private String location;
    private String state;
}
