package com.project.cuchosmarket.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Product {
    @Id
    private String name;

    private String code;
    private String description;
    private LocalDate entryDate;
    private float price;
    private String brand;
    private List<String> images;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonIgnore
    private Category category;

    @ManyToMany(mappedBy = "products")
    @JsonIgnore
    private List<Promotion> promotions;

    public Product(String name, String code, String description, LocalDate entryDate, float price, String brand, Category category, List<String> images) {
        this.name = name;
        this.code = code;
        this.description = description;
        this.entryDate = entryDate;
        this.price = price;
        this.brand = brand;
        this.category = category;
        this.images = images;
    }
}
