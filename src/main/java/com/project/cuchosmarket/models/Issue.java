package com.project.cuchosmarket.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Issue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String userEmail;
    private LocalDate creationDate;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    public Issue(String title, String description, String userEmail, LocalDate creationDate, Order order) {
        this.title = title;
        this.description = description;
        this.userEmail = userEmail;
        this.creationDate = creationDate;
        this.order = order;
    }
}
