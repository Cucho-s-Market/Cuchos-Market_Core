package com.project.cuchosmarket.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DtItem {
    private String name;
    private float unitPrice;
    private float finalPrice;
    private int quantity;
}
