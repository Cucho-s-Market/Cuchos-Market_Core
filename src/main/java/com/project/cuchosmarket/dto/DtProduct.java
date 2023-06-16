package com.project.cuchosmarket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DtProduct {
    private String code;
    private String name;
    private String description;
    private float price;
    private LocalDate entryDate;
    private String brand;
    private Long categoryId;
    private List<String> images;
    private int quantity;

    public DtProduct(String code, String name, String description, float price, LocalDate entryDate, String brand, Long categoryId, List<String> images) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.price = price;
        this.entryDate = entryDate;
        this.brand = brand;
        this.categoryId = categoryId;
        this.images = images;
    }
}
