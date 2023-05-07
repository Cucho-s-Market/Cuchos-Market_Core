package com.project.cuchosmarket.repositorios;

import com.project.cuchosmarket.dominio.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepositorio extends JpaRepository<Categoria, String> {
}
