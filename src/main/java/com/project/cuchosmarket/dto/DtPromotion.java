package com.project.cuchosmarket.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class DtPromotion {
    private Long id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private String image;
    private List<DtProduct> products;
    private int percentage;
    private int n;
    private int m;
    private String promotionType;
}
