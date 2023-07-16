package com.project.cuchosmarket;

import com.project.cuchosmarket.dto.DtProduct;
import com.project.cuchosmarket.dto.DtStock;
import com.project.cuchosmarket.exceptions.*;
import com.project.cuchosmarket.models.*;
import com.project.cuchosmarket.repositories.*;
import com.project.cuchosmarket.services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductServiceTest {
    @Autowired
    private ProductService productService;

    @MockBean
    private ProductRepository productRepository;
    @MockBean
    private EmployeeRepository employeeRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private CategoryRepository categoryRepository;
    @MockBean
    private BranchRepository branchRepository;
    @MockBean
    private StockRepository stockRepository;
    @MockBean
    private PromotionRepository promotionRepository;
    @MockBean
    private ItemRepository itemRepository;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product("Producto", "PROD456", "Descripcion", null, 60.0f,
                "BRAND", null, null);
    }

    @DisplayName("Agregar producto")
    @Test
    void testAddProduct() throws ProductExistException, InvalidProductException, CategoryNotExistException {
        DtProduct dtProduct = new DtProduct("PROD456", "Producto", "Descripcion", 60.0f,
                60.0f, LocalDate.now(), "BRAND", 1L, null, false, 0, 0);

        when(productRepository.existsByName(any())).thenReturn(false);
        when(categoryRepository.findById(any())).thenReturn(Optional.of(new Category()));

        productService.addProduct(dtProduct);
        verify(productRepository, times(1)).save(any());
    }

    @DisplayName("Agregar producto: Informacion invalida")
    @Test
    void testAddProductInvalidProductException() {
        DtProduct dtProduct = new DtProduct("", "Producto", "Descripcion", 0f,
                60.0f, LocalDate.now(), "BRAND", 1L, null, false, 0, 0);
        assertThrows(InvalidProductException.class, () -> productService.addProduct(dtProduct));
    }

    @DisplayName("Editar producto")
    @Test
    void testUpdateProduct() throws ProductNotExistException, InvalidProductException, CategoryNotExistException {
        DtProduct dtProduct = new DtProduct("PROD456", "Producto", "Descripcion", 60.0f,
                60.0f, LocalDate.now(), "BRAND", 1L, null, false, 0, 0);

        when(categoryRepository.findById(any())).thenReturn(Optional.of(new Category()));
        when(productRepository.findById(any())).thenReturn(Optional.ofNullable(product));

        productService.updateProduct(dtProduct);
        verify(productRepository, times(1)).save(any());
    }

    @DisplayName("Borrar producto")
    @Test
    void testDeleteProduct() throws ProductNotExistException {
        DtProduct dtProduct = new DtProduct("PROD456", "Producto", "Descripcion", 60.0f,
                60.0f, LocalDate.now(), "BRAND", 1L, null, false, 0, 0);

        when(productRepository.findById(any())).thenReturn(Optional.ofNullable(product));
        product.setPromotions(new ArrayList<>());

        productService.deleteProduct(dtProduct);
        verify(productRepository, times(1)).delete(product);
    }

    @DisplayName("Listar productos")
    @Test
    void testGetProducts() {
        List<DtProduct> dtProducts = List.of(DtProduct.builder()
                .name(product.getName())
                .code(product.getCode())
                .build());
        Page<DtProduct> products = new PageImpl<>(dtProducts);

        when(productRepository.findProducts(eq(null), eq(null), eq(null), eq(null), eq(null),
                eq(false), any(Pageable.class))).thenReturn(products);

        Page<DtProduct> salida = productService.getProducts(1, 5, null, null, null,
                null, null, null, false, "name", "desc");

        assertNotNull(salida);
        assertEquals(dtProducts.size(), salida.getTotalElements());
    }

    @DisplayName("Actualizar stock de un producto en una sucursal")
    @Test
    void testUpdateStockProduct() throws NoStockException, EmployeeNotWorksInException, ProductNotExistException, UserNotExistException {
        Employee employee = new Employee("Tomas", "Lopez", "tommy@mail.com","password", Branch.builder()
                .id(1L).build());

        when(productRepository.findById(any())).thenReturn(Optional.ofNullable(product));
        when(userRepository.findByEmail(any())).thenReturn(employee);
        when(employeeRepository.findById(any())).thenReturn(Optional.of(employee));
        when(stockRepository.findById(any())).thenReturn(Optional.of(new Stock()));

        productService.updateStockProduct("tommy@mail.com", new DtStock("Producto", 1L, 10));
        verify(stockRepository, times(1)).save(any());
    }
}