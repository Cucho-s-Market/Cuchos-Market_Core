package com.project.cuchosmarket.controllers;

import com.project.cuchosmarket.dto.DtProduct;
import com.project.cuchosmarket.dto.DtResponse;
import com.project.cuchosmarket.exceptions.CategoryNotExist;
import com.project.cuchosmarket.exceptions.ProductInvalidException;
import com.project.cuchosmarket.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;

    @PostMapping("/product/new-product")
    public DtResponse addProduct(@RequestBody DtProduct newProduct) throws CategoryNotExist, ProductInvalidException {
        try {
            productService.addProduct(newProduct);
        }
        catch (CategoryNotExist | ProductInvalidException e) {
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
}
