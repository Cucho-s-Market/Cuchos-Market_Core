package com.project.cuchosmarket.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DtStock {
    private String product_id;
    private Long branch_id;
    private int quantity;
}
