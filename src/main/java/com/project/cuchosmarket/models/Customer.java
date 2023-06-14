package com.project.cuchosmarket.models;

import com.project.cuchosmarket.enums.Role;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Customer extends User {
    private LocalDate birthdate;
    private long telephone;
    private long dni;
    private boolean disabled;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "client_id")
    private List<Address> addresses;

    public Customer(String firstName, String lastName, String email, String password, LocalDate birthdate, long telephone,long dni) {
        super(firstName, lastName, email, password, Role.CUSTOMER);
        this.birthdate = birthdate;
        this.telephone = telephone;
        this.dni = dni;
        this.disabled = false;
        this.addresses = new ArrayList<>();
    }
    public void addAddress(Address address){
        this.addresses.add(address);
    }

    public Boolean removeAddress(Long addressId) {
        return this.addresses.removeIf(address -> address.getId().equals(addressId));
    }
}
