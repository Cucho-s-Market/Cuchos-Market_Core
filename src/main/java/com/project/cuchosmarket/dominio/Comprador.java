package com.project.cuchosmarket.dominio;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Entity
public class Comprador extends Usuario {
    private LocalDate fechaNac;
    private int telefono;
    private boolean bloqueado;

    @OneToMany(orphanRemoval = true)
    private List<Domicilio> domicilios;

    @OneToMany
    private Map<String, Pedido> pedidosRealizados;
}
