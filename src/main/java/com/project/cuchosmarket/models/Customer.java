package com.project.cuchosmarket.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Entity
public class Customer extends User {
    private LocalDate birthdate;
    private int telephone;
    private boolean disabled;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "client_id")
    private List<Address> addresses;

    @OneToMany
    @JoinColumn(name = "client_id")
    private Map<String, Order> ordersPlaced;
}
