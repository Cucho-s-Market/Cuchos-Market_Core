package com.project.cuchosmarket.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Customer extends User {
    private LocalDate birthdate;
    private int telephone;
    private int dni;
    private boolean disabled;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "client_id")
    private List<Address> addresses;

    @OneToMany
    @JoinColumn(name = "client_id")
    private Map<String, Order> ordersPlaced;


    public Customer(String firstName, String lastName, String email, String password, String password1, LocalDate birthdate, int telephone,int dni) {
        super(firstName, lastName, email, password, password1);
        this.birthdate = birthdate;
        this.telephone = telephone;
        this.dni = dni;
        this.disabled = true;
    }
}

