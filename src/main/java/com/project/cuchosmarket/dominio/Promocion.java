package com.project.cuchosmarket.dominio;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public abstract class Promocion {
    @Id
    private String id;
    private String nombre;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    @OneToMany //TODO Checkear Cascada
    private List<Producto> productos;
}
