package com.project.cuchosmarket.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DtItem {
    private String name;
    private float unitPrice;
    private float finalPrice;
    private int quantity;
}
