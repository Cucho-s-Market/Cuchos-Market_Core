package com.project.cuchosmarket;

import com.project.cuchosmarket.dto.DtProduct;
import com.project.cuchosmarket.dto.DtResponse;
import com.project.cuchosmarket.exceptions.CategoryNotExistException;
import com.project.cuchosmarket.exceptions.InvalidProductException;
import com.project.cuchosmarket.exceptions.ProductExistException;
import com.project.cuchosmarket.models.Branch;
import com.project.cuchosmarket.models.Category;
import com.project.cuchosmarket.models.Product;
import com.project.cuchosmarket.models.Stock;
import com.project.cuchosmarket.repositories.BranchRepository;
import com.project.cuchosmarket.repositories.CategoryRepository;
import com.project.cuchosmarket.repositories.ProductRepository;
import com.project.cuchosmarket.repositories.StockRepository;
import com.project.cuchosmarket.services.ProductService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private BranchRepository branchRepository;

    @Mock
    private StockRepository stockRepository;
    @Captor
    private ArgumentCaptor<Product> productCaptor;

    @Test
    public void testAddProduct() throws CategoryNotExistException, InvalidProductException, ProductExistException {
        // Arrange
        List<String> images = new ArrayList<>();
        images.add("image1.jpg");
        images.add("image2.jpg");

        Category category = new Category();
        category.setId(1L);
        category.setName("Category Name");

        // Configurar el objeto dtProduct con el ID de categoría válido
        DtProduct dtProduct = new DtProduct("P123", "pan", "Product description", 10.2f, LocalDate.of(1999, 10, 15), "1L", 1L, images);

        // Configurar el mock categoryRepository
        when(categoryRepository.findById(dtProduct.getCategoryId())).thenReturn(Optional.of(category));

        // Configurar el mock productRepository
        when(productRepository.existsByName(dtProduct.getName())).thenReturn(false);

        // Configurar el mock branchRepository
        List<Branch> branches = new ArrayList<>();
        // Agregar las sucursales necesarias a la lista branches
        when(branchRepository.findAll()).thenReturn(branches);

        // Act
        productService.addProduct(dtProduct);

        // Assert
        verify(productRepository).save(any(Product.class));
        verify(stockRepository, times(branches.size())).save(any(Stock.class));

        verify(productRepository).save(productCaptor.capture());
        Product savedProduct = productCaptor.getValue();
        System.out.println("Product saved: " + savedProduct);
    }

    @Test
    public void testGetProductsBy() {
        // Arrange
        String name = "pan";
        String brand = "Marca";
        Long categoryId = 1L;
        String orderBy = "name";
        String orderDirection = "asc";

        // Create an example list of products expected to be returned
        List<Product> expectedProducts = new ArrayList<>();
        expectedProducts.add(new Product("P123", "pan", "Product description", LocalDate.of(1999, 10, 15), 10.2f, "1L", new Category(), new ArrayList<>()));

        // Configure the productRepository mock to return the example products
        when(productRepository.findAll(any(Specification.class))).thenReturn(expectedProducts);

        // Act
        List<Product> actualProducts = productService.getProductsBy(name, brand, categoryId, orderBy, orderDirection);

        // Assert
        verify(productRepository).findAll(any(Specification.class));
        assertEquals(expectedProducts, actualProducts);

        for (Product product : actualProducts) {
            System.out.println("Product Code: " + product.getCode());
            System.out.println("Product Name: " + product.getName());
            System.out.println("Product Description: " + product.getDescription());
            System.out.println("Product Entry Date: " + product.getEntryDate());
            System.out.println("Product Price: " + product.getPrice());
            System.out.println("Product Brand: " + product.getBrand());
            System.out.println("Product Category: " + product.getCategory().getName());
            System.out.println("Product Images: " + product.getImages());
            System.out.println("----------------------------------------");
        }
    }
}










