package com.project.cuchosmarket.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Map;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String image;
    private Long categoryParent;

    @OneToMany(mappedBy = "category")
    private Map<String, Product> products;


    public Category(Long id, String name, String description, String image, Long categoryParent) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.categoryParent = categoryParent;
    }

    public Category(String name, String description, String image) {
        this.name = name;
        this.description = description;
        this.image = image;
    }



}
