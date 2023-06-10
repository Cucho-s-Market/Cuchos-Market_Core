package com.project.cuchosmarket;

import com.project.cuchosmarket.dto.DtCategory;
import com.project.cuchosmarket.exceptions.InvalidCategoryException;
import com.project.cuchosmarket.models.Category;
import com.project.cuchosmarket.repositories.CategoryRepository;
import com.project.cuchosmarket.services.CategoryService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;

import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @Mock

    private CategoryService servicioMock;

    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private DtCategory categoriaMock;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        categoriaMock = Mockito.mock(DtCategory.class);
    }
/*
    @Test
    public void testAgregarCategoria() {
/*
        // Configuración del mock
        String nombreCategoria = "Electrónica";
        String descripcionCategoria = "Productos electrónicos";

        when(categoriaMock.getName()).thenReturn(nombreCategoria);
        when(categoriaMock.getDescription()).thenReturn(descripcionCategoria);

        // Ejecutar el método bajo prueba
        DtCategory categoriaConvertida = convertirDtCategoriaACategoria(categoriaMock);

        servicioMock.addCategory(categoriaConvertida);

        // Verificar que el método fue llamado con los argumentos esperados
        if (nombreCategoria != null && !nombreCategoria.isEmpty()
                && descripcionCategoria != null && !descripcionCategoria.isEmpty()) {
            verify(servicioMock).addCategory(categoriaConvertida);
        } else {
            verify(servicioMock, never()).addCategory(categoriaConvertida);
        }
    }

        private Category convertirDtCategoriaACategoria (DtCategory dtCategoria){
            // Lógica de conversión de DtCategoria a Categoria
            // Implementa la lógica de conversión adecuada según la estructura y los campos de tus objetos

            Category categoria = new Category();
            categoria.setName(dtCategoria.getName());
            categoria.setDescription(dtCategoria.getDescription());

            return categoria;
        }*/
    }
