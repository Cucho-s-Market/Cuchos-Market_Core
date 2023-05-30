package com.project.cuchosmarket.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
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

    @ManyToOne
    @JoinColumn(name = "category_parent_id")
    private Category categoryParent;

    @OneToMany(mappedBy = "categoryParent", cascade = CascadeType.ALL)
    private List<Category> subcategories;

    @OneToMany(mappedBy = "category")
    private Map<String, Product> products;
}
