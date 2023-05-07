package com.project.cuchosmarket.dominio;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Categoria {
    @Id
    private String id;
    private String nombre;
    private String descripcion;
    private List<Categoria> subcategorias;

    @OneToMany(mappedBy = "categoria")
    private Map<String, Producto> productos;
}
