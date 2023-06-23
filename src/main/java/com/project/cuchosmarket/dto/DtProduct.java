package com.project.cuchosmarket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DtProduct {
    private String code;
    private String name;
    private String description;
    private float price;
    private float finalPrice;
    private LocalDate entryDate;
    private String brand;
    private Long categoryId;
    private List<String> images;
    private int quantity;
    private boolean nxmPromotion;
    private Integer n;
    private Integer m;

    public DtProduct(String code, String name, String description, float price, float finalPrice, LocalDate entryDate,
                     String brand, Long categoryId, List<String> images, boolean nxmPromotion, Integer n, Integer m) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.price = price;
        this.finalPrice = finalPrice;
        this.entryDate = entryDate;
        this.brand = brand;
        this.categoryId = categoryId;
        this.images = images;
        this.nxmPromotion = nxmPromotion;
        this.n = n;
        this.m = m;
    }
}
