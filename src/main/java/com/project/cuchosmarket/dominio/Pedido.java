package com.project.cuchosmarket.dominio;

import com.project.cuchosmarket.enums.EstadoCompra;
import com.project.cuchosmarket.enums.TipoPedido;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Pedido {
    @Id
    private String id;
    private float costo;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    @Enumerated(EnumType.STRING)
    private EstadoCompra estado;

    @Enumerated(EnumType.STRING)
    private TipoPedido tipoPedido;

//    @ManyToOne
//    @JoinColumn(name = "cliente_id")
//    private Comprador cliente;


}
