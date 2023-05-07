package com.project.cuchosmarket.dominio;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Producto {
    @Id
    private String codigo;
    private String nombre;
    private String descripcion;
    private LocalDate fechaIngreso;
    private float precio;
    private float peso;
    private String color;
    private String marca;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;
}
