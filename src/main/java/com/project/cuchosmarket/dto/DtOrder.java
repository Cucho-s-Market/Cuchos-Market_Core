package com.project.cuchosmarket.dto;

import com.project.cuchosmarket.enums.OrderStatus;
import com.project.cuchosmarket.enums.OrderType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DtOrder {
    private Long branchId;
    private Long id;
    private float totalPrice;
    private LocalDate creationDate;
    private LocalDate endDate;
    private OrderStatus status;
    private Long addressId;
    private String clientAddress;
    private OrderType type;
    private List<DtItem> products;

    public DtOrder(Long branchId, Long id, float totalPrice, LocalDate creationDate, LocalDate endDate, OrderStatus status,
                   String clientAddress, OrderType type) {
        this.branchId = branchId;
        this.id = id;
        this.totalPrice = totalPrice;
        this.creationDate = creationDate;
        this.endDate = endDate;
        this.status = status;
        this.clientAddress = clientAddress;
        this.type = type;
    }

    public DtOrder(Long id, float totalPrice, LocalDate creationDate, LocalDate endDate, OrderStatus status, String clientAddress,
                   OrderType type) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.creationDate = creationDate;
        this.endDate = endDate;
        this.status = status;
        this.clientAddress = clientAddress;
        this.type = type;
    }
}
