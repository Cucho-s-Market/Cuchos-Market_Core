package com.project.cuchosmarket.dominio;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Descuento extends Promocion {
    private int porcentaje;
}
