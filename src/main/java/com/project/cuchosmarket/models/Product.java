package com.project.cuchosmarket.models;

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
    @GeneratedValue(strategy = GenerationType.UUID)
    private String code;

    private String name;
    private String description;
    private LocalDate entryDate;
    private float price;
    private String brand;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private List<String> images;
}
