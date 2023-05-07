package com.project.cuchosmarket.dominio;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Domicilio { //TODO: cual es el id?
    @Id
    private String direccion;
    private int nroPuerta;
    private String localidad;
    private String departamento;
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Comprador comprador;
}
