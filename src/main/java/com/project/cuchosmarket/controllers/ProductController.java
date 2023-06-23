package com.project.cuchosmarket.controllers;

import com.project.cuchosmarket.dto.DtProduct;
import com.project.cuchosmarket.dto.DtResponse;
import com.project.cuchosmarket.dto.DtStock;
import com.project.cuchosmarket.exceptions.*;
import com.project.cuchosmarket.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public DtResponse getProducts(@RequestParam(value = "page_number", required = false, defaultValue = "0") int page_number,
                                  @RequestParam(value = "page_size", required = false, defaultValue = "50") int page_size,
                                  @RequestParam(value = "branch_id", required = false)  Long branch_id,
                                  @RequestParam(value = "code", required = false) String code,
                                  @RequestParam(value = "name", required = false) String name,
                                  @RequestParam(value = "brand", required = false) String brand,
                                  @RequestParam(value = "category_id", required = false) Long category_id,
                                  @RequestParam(value = "promotion_id", required = false) Long promotion_id,
                                  @RequestParam(value = "includeExpiredPromotion", required = false, defaultValue = "false") boolean includeExpiredPromotion,
                                  @RequestParam(value = "orderBy", required = false, defaultValue = "name") String orderBy,
                                  @RequestParam(value = "orderDirection", required = false, defaultValue = "asc") String orderDirection) {
        Page<DtProduct> productsList = productService.getProducts(page_number, page_size, branch_id, code, name, brand,
                category_id, promotion_id, includeExpiredPromotion, orderBy, orderDirection);
        return DtResponse.builder()
                .error(false)
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
