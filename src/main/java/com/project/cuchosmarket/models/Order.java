package com.project.cuchosmarket.models;

import com.project.cuchosmarket.enums.OrderStatus;
import com.project.cuchosmarket.enums.OrderType;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private float totalPrice;
    private LocalDate creationDate;
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Enumerated(EnumType.STRING)
    private OrderType type;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Item> products;

    @ManyToOne
    @JoinColumn(name = "client_address_id")
    private Address clientAddress;

    public Order(float totalPrice, LocalDate creationDate, OrderStatus status, OrderType type, List<Item> products) {
        this.totalPrice = totalPrice;
        this.creationDate = creationDate;
        this.status = status;
        this.type = type;
        this.products = products;
    }
}
