package com.project.cuchosmarket;

import com.project.cuchosmarket.dto.DtCategory;
import com.project.cuchosmarket.models.Category;
import com.project.cuchosmarket.repositories.CategoryRepository;
import com.project.cuchosmarket.services.CategoryService;

import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.testng.AssertJUnit.assertNotNull;


@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CategoryServiceTest{

    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private CategoryService categoryService;


    @DisplayName("Dada una lista de categorias " + "cuando llamamos a 'getCategories' "
    + "esperamos q la lista este cargada")


       /* @Test
        public void testValidateCategory(){
            DtCategory dtCategorySimulado = new DtCategory(1l, "electronica", "cables", "carlos@", 123l);
            DtCategory dtCategoryEsperado = new DtCategory(1l, "electronica", "cables", "carlos@", 123l);


            Mockito.when(categoryRepository.findByName(dtCategorySimulado.getName()).thenReturn(dtCategorySimulado));

        }*/

        @Test
        public void testGetListCategories() {
            List<Category> categories = Arrays.asList(
                    new Category("electronica1", "cables", "carlos@"),
                    new Category("electronica", "disipadores", "carlos@")
            );

        assertNotNull(categoryRepository);

        // Configurar el comportamiento del mock
        when(categoryRepository.findAll()).thenReturn(categories);

            List<DtCategory> resultado = categoryService.getCategories();
            Assert.assertEquals(resultado.size(),categories.size());
         // Assertions.assertEquals(resultado.get(0).getDescription(), categories.get(0).getDescription());
        // Assertions.assertEquals(resultado.get(1).getId(), categories.get(1).getId());


        }

    }

