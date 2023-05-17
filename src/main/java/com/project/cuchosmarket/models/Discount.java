package com.project.cuchosmarket.models;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Discount extends Promotion {
    private int percentage;
}
