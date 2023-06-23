package com.project.cuchosmarket.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;
    private float unitPrice;
    private float finalPrice;
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "product_name")
    private Product product;

    public Item(String name, float unitPrice, float finalPrice, int quantity, Product product) {
        this.name = name;
        this.unitPrice = unitPrice;
        this.finalPrice = finalPrice;
        this.quantity = quantity;
        this.product = product;
    }
}
