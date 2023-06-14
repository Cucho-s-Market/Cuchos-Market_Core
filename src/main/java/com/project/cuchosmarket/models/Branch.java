package com.project.cuchosmarket.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String address;

    @OneToMany
    @JoinColumn(name = "branch_id")
    private List<Order> orders;

    public void addOrder(Order order){
        this.orders.add(order);
    }
}
