package com.project.cuchosmarket.dominio;

import jakarta.persistence.CascadeType;
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

    @OneToMany(mappedBy = "comprador", orphanRemoval = true) //TODO tiene sentido que sea bidireccional?
    private List<Domicilio> domicilios;

//    @OneToMany(mappedBy = "cliente")
//    private List<Pedido> pedidosRealizados;
    @OneToMany
    private Map<String, Pedido> pedidosRealizados;
}
