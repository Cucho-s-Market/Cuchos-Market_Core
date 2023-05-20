package com.project.cuchosmarket.services;

import com.project.cuchosmarket.dto.DtProduct;
import com.project.cuchosmarket.exceptions.CategoryNotExist;
import com.project.cuchosmarket.exceptions.ProductExistException;
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

    private void validateProduct(DtProduct dtProduct) throws ProductInvalidException, ProductExistException {
        if(dtProduct.getCode().isEmpty() | dtProduct.getName().isEmpty() | dtProduct.getPrice() <= 0 | dtProduct.getEntryDate() == null) throw new ProductInvalidException();

        if(productRepository.existsByName(dtProduct.getName())) throw new ProductExistException();
    }

    public void addProduct(DtProduct dtProduct) throws CategoryNotExist, ProductInvalidException, ProductExistException {
        Optional<Category> category = categoryRepository.findById(dtProduct.getCategoryId());

        if(category.isEmpty()) throw new CategoryNotExist();

        validateProduct(dtProduct);

        Product product = new Product(
                dtProduct.getName(),
                dtProduct.getCode(),
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
