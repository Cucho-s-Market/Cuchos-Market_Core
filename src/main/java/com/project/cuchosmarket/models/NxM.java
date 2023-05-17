package com.project.cuchosmarket.models;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class NxM extends Promotion {
    private int n;
    private int m;
}
