package com.project.cuchosmarket.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String address;
    private int doorNumber;
    private String location;
    private String state;

    public Address(String address, int doorNumber, String location, String state) {
        this.address = address;
        this.doorNumber = doorNumber;
        this.location   = location;
        this.state = state;
    }

    @Override
    public String toString() {
        return address + " " + doorNumber + ", " + location+ ", "  + state;
    }
}
