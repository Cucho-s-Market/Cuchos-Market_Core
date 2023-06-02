package com.project.cuchosmarket.dto;

import com.project.cuchosmarket.enums.OrderStatus;
import com.project.cuchosmarket.enums.OrderType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class DtOrder {
    private Long branchId;
    private Long id;
    private OrderStatus status;
    private OrderType type;
    private List<DtItem> products;
}
