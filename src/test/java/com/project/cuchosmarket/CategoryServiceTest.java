package com.project.cuchosmarket;

import com.project.cuchosmarket.dto.DtCategory;
import com.project.cuchosmarket.exceptions.InvalidCategoryException;
import com.project.cuchosmarket.models.Category;
import com.project.cuchosmarket.repositories.CategoryRepository;
import com.project.cuchosmarket.services.CategoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@SpringBootTest
@AutoConfigureMockMvc
public class CategoryServiceTest {

    @MockBean
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryService categoryService;


    @DisplayName("Agregar una subcategoria")
    @Test
    public void testAddCategory() throws InvalidCategoryException {
        DtCategory dtCategory = new DtCategory(1L,"electronica1", "cables", "carlos@", 123L);
        when(categoryRepository.findById(dtCategory.getCategoryParent())).thenReturn(Optional.of(new Category()));
        categoryService.addCategory(dtCategory);

        verify(categoryRepository, times(1)).findById(dtCategory.getCategoryParent());
        verify(categoryRepository, times(2)).save(any(Category.class));

    }

    @DisplayName("Categoria con nombre repetido")
    @Test
    public void testAddCategoryAlreadyExist() {
        DtCategory dtCategory = new DtCategory(1L,"electronica1", "cables", "carlos@", 0L);
        when(categoryRepository.existsByName(any())).thenReturn(true);

        assertThrows(InvalidCategoryException.class, () -> categoryService.addCategory(dtCategory));
    }

    @DisplayName("Categoria invalida")
    @Test
    public void testAddCategoryThrowExceptionInvalidCategory() {
        DtCategory dtCategory = new DtCategory(1L,"", "", "carlos@", 0L);
        when(categoryRepository.existsByName(any())).thenReturn(true);

        assertThrows(InvalidCategoryException.class, () -> categoryService.addCategory(dtCategory));
    }

    @DisplayName("Listar categorias")
    @Test
    public void testGetListCategories() {
        List<Category> categories = Arrays.asList(
                new Category(1L,"electronica1", "cables", "carlos@",0L),
                new Category(2L, "electronica", "disipadores", "carlos@", 1L)
        );

        when(categoryRepository.findAll()).thenReturn(categories);

        List<DtCategory> resultado = categoryService.getCategories();

        assertEquals(resultado.size(),categories.size());
        assertEquals(resultado.get(0).getDescription(), categories.get(0).getDescription());
        assertEquals(resultado.get(1).getId(), categories.get(1).getId());
    }

}

