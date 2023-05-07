package com.project.cuchosmarket.dominio;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Stock {
    private String sucursalId;
    private String productoCodigo;
    private int cantidad;
}
