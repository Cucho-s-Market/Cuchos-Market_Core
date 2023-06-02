package com.project.cuchosmarket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class DtCategory {
    private Long id;
    private String name;
    private String description;
    private String image;
    private Long categoryParent;
    private List<DtCategory> subcategories;
}
