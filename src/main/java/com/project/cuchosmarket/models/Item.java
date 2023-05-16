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
    private Long id;

    private String name;
    private Float price;
    private Float finalPrice;
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "product_code")
    private Product product;
}
