package com.project.cuchosmarket.services;

import com.project.cuchosmarket.dto.DtProduct;
import com.project.cuchosmarket.dto.DtStock;
import com.project.cuchosmarket.exceptions.*;
import com.project.cuchosmarket.models.*;
import com.project.cuchosmarket.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BranchRepository branchRepository;
    private final StockRepository stockRepository;
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;

    private void validateProduct(DtProduct dtProduct) throws InvalidProductException {
        if(dtProduct.getCode() == null | dtProduct.getName() == null | dtProduct.getCategoryId() == null | dtProduct.getEntryDate() == null) throw new InvalidProductException();

        if(dtProduct.getCode().isBlank()) throw new InvalidProductException("Codigo de producto invalido.");
        if(dtProduct.getName().isBlank() || dtProduct.getName().length() > 50) throw new InvalidProductException("Nombre de producto invalido.");
        if(dtProduct.getDescription().isBlank() || dtProduct.getDescription().length() > 50) throw new InvalidProductException("Descripcion de producto invalido.");
        if(dtProduct.getPrice() <= 0) throw new InvalidProductException("Precio de producto invalido.");
    }

    private Product findProduct(DtProduct dtProduct) throws ProductNotExistException {
        Optional<Product> product = productRepository.findById(dtProduct.getName());
        if(product.isEmpty()) throw new ProductNotExistException(dtProduct.getName());

        return product.get();
    }

    @Transactional
    public void addProduct(DtProduct dtProduct) throws CategoryNotExistException, InvalidProductException, ProductExistException {
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
        List<Branch> branches = branchRepository.findAll();
        for (Branch branch : branches) {
            Stock stock = new Stock(new StockId(product, branch), 0);
            stockRepository.save(stock);
        }
    }

    public void updateProduct(DtProduct dtProduct) throws ProductNotExistException, InvalidProductException {
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
        Product product = findProduct(dtProduct);

        branchRepository.findAll().forEach(branch -> stockRepository.findById(new StockId(product, branch)).ifPresent(stockRepository::delete));
        productRepository.delete(product);
    }
  
    public Page<DtProduct> getProducts(int pageNumber, int pageSize, Long branchId, String code, String name, String brand, Long category_id, String orderBy, String orderDirection) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortBy(orderBy, orderDirection));
        if (branchId != null) return stockRepository.findProducts(branchId, code, name, brand, category_id, pageable);
        else return productRepository.findProducts(code, name, brand, category_id, pageable);
    }

    private Sort sortBy(String orderBy, String orderDirection) {
        Sort.Direction direction = null;
        Sort sort = null;

        if(orderDirection.equalsIgnoreCase("desc")) {
            direction = Sort.Direction.DESC;
        } else {
            direction = Sort.Direction.ASC;
        }

        if (orderBy.equals("name") || orderBy.equals("price") || orderBy.equals("entryDate")) {
            sort = Sort.by(direction, orderBy);
        } else {
            sort = Sort.by(direction, "name");
        }

        return sort;
    }

    public void updateStockProduct(String userEmail, DtStock stockProduct) throws EmployeeNotWorksInException, ProductNotExistException, UserNotExistException, NoStockException {
        Product product = findProduct(DtProduct.builder().name(stockProduct.getProduct_id()).build());

        User employee = userRepository.findByEmail(userEmail);
        Branch branchEmployee = employeeRepository.findById(employee.getId()).orElseThrow(UserNotExistException::new).getBranch();

        if (!branchEmployee.getId().equals(stockProduct.getBranch_id())) throw new EmployeeNotWorksInException();

        if (stockProduct.getQuantity() < 0) throw new IllegalArgumentException("Cantidad de producto invalida.");

        Optional<Stock> stock = stockRepository.findById(new StockId(product, branchEmployee));

        if(stock.isPresent()) {
            stock.get().setQuantity(stockProduct.getQuantity());
            stockRepository.save(stock.get());
        }
        else throw new NoStockException("No se encontró el stock del producto.");
    }

}
