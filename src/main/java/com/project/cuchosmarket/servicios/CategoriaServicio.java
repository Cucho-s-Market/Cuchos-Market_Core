package com.project.cuchosmarket.servicios;

import com.project.cuchosmarket.repositorios.CategoriaRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoriaServicio {
    private final CategoriaRepositorio categoriaRepositorio;
}
