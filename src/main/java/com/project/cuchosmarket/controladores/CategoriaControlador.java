package com.project.cuchosmarket.controladores;

import com.project.cuchosmarket.servicios.CategoriaServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/categorias")
public class CategoriaControlador {
    private final CategoriaServicio categoriaServicio;


}
