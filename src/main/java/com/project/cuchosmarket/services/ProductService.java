package com.project.cuchosmarket.services;

import com.project.cuchosmarket.dto.DtProduct;
import com.project.cuchosmarket.exceptions.CategoryNotExist;
import com.project.cuchosmarket.exceptions.ProductInvalidException;
import com.project.cuchosmarket.models.Category;
import com.project.cuchosmarket.models.Product;
import com.project.cuchosmarket.repositories.CategoryRepository;
import com.project.cuchosmarket.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    private Boolean validateProduct(DtProduct dtProduct) {
        return (
                    dtProduct.getCode().isEmpty() |
                    dtProduct.getName().isEmpty() |
                    dtProduct.getPrice() <= 0 |
                    dtProduct.getEntryDate() != null
        );
    }

    public void addProduct(DtProduct dtProduct) throws CategoryNotExist, ProductInvalidException {
        Optional<Category> category = categoryRepository.findById(dtProduct.getCategoryId());

        if(category.isEmpty()) throw new CategoryNotExist();
        if(validateProduct(dtProduct)) throw new ProductInvalidException();

        Product product = new Product(
                dtProduct.getCode(),
                dtProduct.getName(),
                dtProduct.getDescription(),
                dtProduct.getEntryDate(),
                dtProduct.getPrice(),
                dtProduct.getBrand(),
                category.get(),
                dtProduct.getImages()
        );

        productRepository.save(product);
    }

}
