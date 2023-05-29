package com.project.cuchosmarket.services;

import com.project.cuchosmarket.dto.DtProduct;
import com.project.cuchosmarket.dto.DtStock;
import com.project.cuchosmarket.exceptions.*;
import com.project.cuchosmarket.models.*;
import com.project.cuchosmarket.repositories.*;
import com.project.cuchosmarket.repositories.specifications.ProductSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final MarketBranchRepository marketBranchRepository;
    private final StockRepository stockRepository;
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;

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

    @Transactional
    public void addProduct(DtProduct dtProduct) throws CategoryNotExistException, ProductInvalidException, ProductExistException {
        Optional<Category> category;

        //Validations
        validateProduct(dtProduct);

        if(productRepository.existsByName(dtProduct.getName())) throw new ProductExistException();

        category = categoryRepository.findById(dtProduct.getCategoryId());

        if(category.isEmpty()) throw new CategoryNotExistException();

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

        createStock(newProduct);
    }

    private void createStock(Product product) {
        List<MarketBranch> marketBranches = marketBranchRepository.findAll();
        for (MarketBranch branch : marketBranches) {
            Stock stock = new Stock(new StockId(product, branch), 0);
            stockRepository.save(stock);
        }
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

    public void deleteProduct(DtProduct dtProduct) throws ProductNotExistException {
        Product product;
        product = findProduct(dtProduct);

        productRepository.delete(product);
    }
  
    public List<Product> getProductsBy(String name, String brand, Long category_id, String orderBy, String orderDirection) {
        Specification<Product> specification = ProductSpecifications.filterByAttributes(name, brand, orderBy, orderDirection, category_id);
        return productRepository.findAll(specification);
    }

    public void updateStockProduct(String userEmail, DtStock stockProduct) throws EmployeeNotWorksInException, ProductNotExistException {
        Product product = findProduct(DtProduct.builder().name(stockProduct.getProduct_id()).build());

        User user = userRepository.findByEmail(userEmail);
        MarketBranch marketBranchEmployee = employeeRepository.findById(user.getId()).get().getMarketBranch();

        if (!marketBranchEmployee.getId().equals(stockProduct.getBranch_id())) throw new EmployeeNotWorksInException();

        if (stockProduct.getQuantity() < 0) throw new IllegalArgumentException("Cantidad de producto invalida.");

        Stock stock = stockRepository.findById(new StockId(product, marketBranchEmployee)).get();
        stock.setQuantity(stockProduct.getQuantity());
        stockRepository.save(stock);
    }
}
