package com.project.cuchosmarket.controllers;

import com.project.cuchosmarket.dto.DtProduct;
import com.project.cuchosmarket.dto.DtResponse;
import com.project.cuchosmarket.dto.DtStock;
import com.project.cuchosmarket.exceptions.*;
import com.project.cuchosmarket.models.Product;
import com.project.cuchosmarket.security.JwtService;
import com.project.cuchosmarket.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public DtResponse getProducts(@RequestParam(value = "name", required = false) String name,
                                  @RequestParam(value = "brand", required = false) String brand,
                                  @RequestParam(value = "category_id", required = false) Long category_id,
                                  @RequestParam(value = "orderBy", required = false) String orderBy,
                                  @RequestParam(value = "orderDirection", required = false) String orderDirection) {
        List<Product> productsList = productService.getProductsBy(name, brand, category_id, orderBy, orderDirection);
        return DtResponse.builder()
                .error(false)
                .message(String.valueOf(productsList.size()))
                .data(productsList)
                .build();
    }

    @PostMapping
    public DtResponse addProduct(@RequestBody DtProduct newProduct) {
        try {
            productService.addProduct(newProduct);
        }
        catch (CategoryNotExistException | InvalidProductException | ProductExistException e) {
            return DtResponse.builder()
                    .error(true)
                    .message(e.getMessage())
                    .build();
        }

        return  DtResponse.builder()
                .error(false)
                .message("Producto agregado correctamente.")
                .build();
    }

    @PutMapping
    public DtResponse updateProduct(@RequestBody DtProduct updatedProduct) {
        try {
            productService.updateProduct(updatedProduct);
        }
        catch (ProductNotExistException | InvalidProductException e) {
            return  DtResponse.builder()
                    .error(true)
                    .message(e.getMessage())
                    .build();
        }

        return  DtResponse.builder()
                .error(false)
                .message("La informacion del producto " + updatedProduct.getName() + " fue actualizada correctamente.")
                .build();
    }

    @DeleteMapping
    public DtResponse deleteProduct(@RequestBody DtProduct productToDelete) {
        try {
            productService.deleteProduct(productToDelete);
        }
        catch (ProductNotExistException e) {
            return  DtResponse.builder()
                    .error(true)
                    .message(e.getMessage())
                    .build();
        }
        return  DtResponse.builder()
                .error(false)
                .message("El producto " + productToDelete.getName() + " fue eliminado correctamente.")
                .build();
    }

    @PutMapping("/employee/stock")
    public DtResponse updateStock(@RequestBody DtStock stockProduct) {
        try {
            String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            productService.updateStockProduct(userEmail, stockProduct);
        } catch (EmployeeNotWorksInException | ProductNotExistException | UserNotExistException | NoStockException e) {
            return DtResponse.builder()
                    .error(true)
                    .message(e.getMessage())
                    .build();
        }
        return DtResponse.builder()
                .error(false)
                .message("Stock del producto " + stockProduct.getProduct_id() + " actualizado con exito.")
                .build();
    }
}
