package com.project.cuchosmarket.models;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@DiscriminatorValue("nxm")
public class NxM extends Promotion {
    private int n;
    private int m;

    public NxM(String name, LocalDate startDate, LocalDate endDate, String image, List<Product> products, int n, int m) {
        super(name, startDate, endDate, image, products);
        this.n = n;
        this.m = m;
    }
}
