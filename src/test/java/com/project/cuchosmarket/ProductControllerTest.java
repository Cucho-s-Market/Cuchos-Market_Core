package com.project.cuchosmarket;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.project.cuchosmarket.controllers.ProductController;
import com.project.cuchosmarket.dto.DtProduct;
import com.project.cuchosmarket.models.Category;
import com.project.cuchosmarket.models.Product;
import com.project.cuchosmarket.services.ProductService;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class ProductControllerTest {

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductService productService;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    public void testGetProducts() throws Exception {
        // Arrange
        String name = "pan";
        String brand = "Marca";
        Long categoryId = 1L;
        String orderBy = "name";
        String orderDirection = "asc";
        List<Product> expectedProducts = new ArrayList<>();
        expectedProducts.add(new Product("pan", "P123", "Product description", LocalDate.of(1999, 10, 15), 10.2f, "1L", new Category(), new ArrayList<>()));

        when(productService.getProductsBy(name, brand, categoryId, orderBy, orderDirection)).thenReturn(expectedProducts);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/products")
                        .param("name", name)
                        .param("brand", brand)
                        .param("category_id", String.valueOf(categoryId))
                        .param("orderBy", orderBy)
                        .param("orderDirection", orderDirection))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].code", Matchers.is("P123")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name", Matchers.is("pan")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].description", Matchers.is("Product description")));
    }

    @Test
    public void testAddProduct() throws Exception {
        // Arrange
        DtProduct newProduct = new DtProduct("P123", "pan", "Product description", 10.2f, LocalDate.of(1999, 10, 15), "1L", 1L, new ArrayList<>());

        // Create a custom ObjectMapper and register the JavaTimeModule
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newProduct)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Producto agregado correctamente.")));

        verify(productService).addProduct(Mockito.refEq(newProduct));
    }

}


