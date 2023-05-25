package com.project.cuchosmarket.services;

import com.project.cuchosmarket.dto.DtProduct;
import com.project.cuchosmarket.exceptions.CategoryNotExist;
import com.project.cuchosmarket.exceptions.ProductExistException;
import com.project.cuchosmarket.exceptions.ProductInvalidException;
import com.project.cuchosmarket.exceptions.ProductNotExistException;
import com.project.cuchosmarket.models.Category;
import com.project.cuchosmarket.models.Product;
import com.project.cuchosmarket.repositories.CategoryRepository;
import com.project.cuchosmarket.repositories.ProductRepository;
import com.project.cuchosmarket.repositories.specifications.ProductSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    private void validateProduct(DtProduct dtProduct) throws ProductInvalidException {
        if(dtProduct.getCode() == null | dtProduct.getName() == null | dtProduct.getCategoryId() == null | dtProduct.getEntryDate() == null) throw new ProductInvalidException();

        if(dtProduct.getCode().isBlank()) throw new ProductInvalidException("Codigo de producto invalido.");
        if(dtProduct.getName().isBlank()) throw new ProductInvalidException("Nombre de producto invalido.");
        if(dtProduct.getPrice() <= 0) throw new ProductInvalidException("Precio de producto invalido.");
    }

    private Product findProduct(DtProduct dtProduct) throws ProductNotExistException {
        Optional<Product> product = productRepository.findById(dtProduct.getName());
        if(product.isEmpty()) throw new ProductNotExistException();

        return product.get();
    }

    public void addProduct(DtProduct dtProduct) throws CategoryNotExist, ProductInvalidException, ProductExistException {
        Optional<Category> category;

        //Validations
        validateProduct(dtProduct);

        if(productRepository.existsByName(dtProduct.getName())) throw new ProductExistException();

        category = categoryRepository.findById(dtProduct.getCategoryId());

        if(category.isEmpty()) throw new CategoryNotExist();

        //New product creation
        Product newProduct = new Product(
                dtProduct.getName(),
                dtProduct.getCode(),
                dtProduct.getDescription(),
                dtProduct.getEntryDate(),
                dtProduct.getPrice(),
                dtProduct.getBrand(),
                category.get(),
                dtProduct.getImages()
        );
        productRepository.save(newProduct);
    }

    public void updateProduct(DtProduct dtProduct) throws ProductNotExistException, ProductInvalidException {
        Product product;
        //Verify and get product
        validateProduct(dtProduct);
        product = findProduct(dtProduct);

        //Update product information
        product.setCode(dtProduct.getCode());
        product.setDescription(dtProduct.getDescription());
        product.setPrice(dtProduct.getPrice());
        product.setEntryDate(dtProduct.getEntryDate());
        product.setBrand(dtProduct.getBrand());
        product.setImages(dtProduct.getImages());

        productRepository.save(product);
    }

    public List<Product> getProductsBy(String name, String brand, Long category_id, String orderBy, String orderDirection) {
        Specification<Product> specification = ProductSpecifications.filterByAttributes(name, brand, orderBy, orderDirection);
        return productRepository.findAll(specification);
    }
}
