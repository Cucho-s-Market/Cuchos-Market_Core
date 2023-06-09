package com.project.cuchosmarket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
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
}
