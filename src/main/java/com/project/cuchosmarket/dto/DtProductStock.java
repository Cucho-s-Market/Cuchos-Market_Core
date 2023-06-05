package com.project.cuchosmarket.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class DtProductStock extends DtProduct {
    private int quantity;
    public DtProductStock(String code, String name, String description, float price, LocalDate entryDate, String brand, Long categoryId, List<String> images, int quantity) {
        super(code, name, description, price, entryDate, brand, categoryId, images);
        this.quantity = quantity;
    }
}
