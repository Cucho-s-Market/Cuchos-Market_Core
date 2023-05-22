package com.project.cuchosmarket.controllers;

import com.project.cuchosmarket.dto.DtProduct;
import com.project.cuchosmarket.dto.DtResponse;
import com.project.cuchosmarket.exceptions.CategoryNotExist;
import com.project.cuchosmarket.exceptions.ProductExistException;
import com.project.cuchosmarket.exceptions.ProductInvalidException;
import com.project.cuchosmarket.exceptions.ProductNotExistException;
import com.project.cuchosmarket.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;

    @PostMapping("/add-product")
    public DtResponse addProduct(@RequestBody DtProduct newProduct) {
        try {
            productService.addProduct(newProduct);
        }
        catch (CategoryNotExist | ProductInvalidException | ProductExistException e) {
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

    @PostMapping("/update-product")
    public DtResponse updateProduct(@RequestBody DtProduct updatedProduct) {

        try {
            productService.updateProduct(updatedProduct);
        }
        catch (ProductNotExistException | ProductInvalidException e) {
            return  DtResponse.builder()
                    .error(false)
                    .message(e.getMessage())
                    .build();
        }

        return  DtResponse.builder()
                .error(false)
                .message("La informacion del producto " + updatedProduct.getName() + " fue actualizada correctamente.")
                .build();
    }
}
