package com.project.cuchosmarket.dominio;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
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

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Imagen> imagenes;
}
