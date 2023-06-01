package com.project.cuchosmarket.controllers;

import com.project.cuchosmarket.dto.DtProduct;
import com.project.cuchosmarket.dto.DtResponse;
import com.project.cuchosmarket.dto.DtStock;
import com.project.cuchosmarket.exceptions.*;
import com.project.cuchosmarket.models.Product;
import com.project.cuchosmarket.security.JwtService;
import com.project.cuchosmarket.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    private final JwtService jwtService;

    @PostMapping("/admin/add-product")
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

    @PutMapping("/admin/update-product")
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

    @DeleteMapping("/admin/delete-product")
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

    @PutMapping("/employee/update-stock")
    public DtResponse updateStock(@RequestHeader("Authorization") String authorizationHeader, @RequestBody DtStock stockProduct) {
        try {
            String userEmail = jwtService.extractUsername(authorizationHeader.substring(7));
            productService.updateStockProduct(userEmail, stockProduct);
        } catch (EmployeeNotWorksInException | ProductNotExistException e) {
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
