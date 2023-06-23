package com.project.cuchosmarket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.cuchosmarket.controllers.CategoryController;
import com.project.cuchosmarket.dto.DtCategory;
import com.project.cuchosmarket.exceptions.InvalidCategoryException;
import com.project.cuchosmarket.services.CategoryService;

import static org.burningwave.core.assembler.StaticComponentContainer.Objects;
import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.Arrays;
import java.util.List;




@RunWith(MockitoJUnitRunner.class)
public class testGetCategoriesController {
    private MockMvc mockMvc;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
    }

    @Test
    public void testGetCategoriesController() throws Exception {
        // Configurar el mock del servicio para devolver la lista de categorías
        List<DtCategory> categories = Arrays.asList(
                new DtCategory(1L, "Categoría 1", "Descripción 1", "imagen1.jpg", null),
                new DtCategory(2L, "Categoría 2", "Descripción 2", "imagen2.jpg", null)
        );
        when(categoryService.getCategories()).thenReturn(categories);

        mockMvc.perform(MockMvcRequestBuilders.get("/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value(false));
    }

    @Test
    public void testAddCategoryValid() throws Exception {
        // Crear una categoría válida
        DtCategory category = new DtCategory(1L, "Almacen", "general", "", null);
        // Convertir la categoría a JSON
        String jsonCategory = new ObjectMapper().writeValueAsString(category);
        // Simular que el servicio no lanza ninguna excepción al agregar la categoría
        doNothing().when(categoryService).addCategory(any(DtCategory.class));
        // Realizar una petición POST con el JSON de la categoría y verificar que el estado sea OK (200)
        mockMvc.perform(MockMvcRequestBuilders.post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCategory))
                .andDo(print())
                .andExpect(status().isOk());
    }

}


