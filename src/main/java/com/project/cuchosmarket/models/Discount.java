package com.project.cuchosmarket.models;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@DiscriminatorValue("discount")
public class Discount extends Promotion {
    private int percentage;

    public Discount(String name, LocalDate startDate, LocalDate endDate, String image, List<Product> products, int percentage) {
        super(name, startDate, endDate, image, products);
        this.percentage = percentage;
    }
}
