package com.project.cuchosmarket.services;

import com.project.cuchosmarket.dto.DtItem;
import com.project.cuchosmarket.dto.DtOrder;
import com.project.cuchosmarket.enums.OrderStatus;
import com.project.cuchosmarket.exceptions.BranchNotExistException;
import com.project.cuchosmarket.exceptions.EmployeeNotWorksInException;
import com.project.cuchosmarket.exceptions.ProductNotExistException;
import com.project.cuchosmarket.exceptions.UserNotExistException;
import com.project.cuchosmarket.models.*;
import com.project.cuchosmarket.repositories.*;
import com.project.cuchosmarket.repositories.specifications.OrderSpecifications;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final CustomerRepository customerRepository;
    private final MarketBranchRepository marketBranchRepository;
    private final ProductRepository productRepository;
    private final StockRepository stockRepository;


    public List<Order> getOrdersBy(String username, Long marketBranchId, String orderStatus, LocalDate startDate,
                                   LocalDate endDate, String orderDirection) throws EmployeeNotWorksInException {
        User user = userRepository.findByEmail(username);
        Branch branchEmployee = employeeRepository.findById(user.getId()).get().getBranch();

        if (!branchEmployee.getId().equals(marketBranchId)) throw new EmployeeNotWorksInException();

        OrderStatus status = null;
        if (orderStatus != null) {
            try {
                status = OrderStatus.valueOf(orderStatus.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Estado de la compra invalido.");
            }
        }

        Specification<Order> specification = OrderSpecifications.filterByAttributes(status, startDate, endDate, orderDirection);
        return orderRepository.findAll(specification);
    }

    @Transactional
    public void buyProducts(Long userId, DtOrder dtOrder) throws BranchNotExistException, UserNotExistException, ProductNotExistException {
        Customer customer = customerRepository.findById(userId).orElseThrow(UserNotExistException::new);
        Branch marketBranch = marketBranchRepository.findById(dtOrder.getId())
                .orElseThrow(() -> new BranchNotExistException(dtOrder.getBranchId()));

        if (dtOrder.getProducts().isEmpty()) {
            throw new IllegalArgumentException("No se seleccionaron ningun producto para comprar.");
        }

        List<Item> items = new ArrayList<>();
        Order order = null;
        for (DtItem dtItem : dtOrder.getProducts()) {
            if (dtItem.getQuantity() < 1) throw new IllegalArgumentException("La cantidad del producto que se encarga ha de ser mayor a 0.");
            Product product = productRepository.findById(dtItem.getName()).orElseThrow(() -> new ProductNotExistException()); //TODO check si es mejor ir guardando los objetos que tiren excepcion o que el 1ero que este mal pare la funcion
            Stock productStock = stockRepository.findById(new StockId(product, marketBranch)).get();

            if (productStock.getQuantity() < 1) {
                //throw new Excepcion de que no hay stock
            } else {
                productStock.setQuantity(productStock.getQuantity()-1);
            }

            Item item = new Item(dtItem.getName(), dtItem.getUnitPrice(), dtItem.getUnitPrice() * dtItem.getQuantity(),
                    dtItem.getQuantity(), product);
            items.add(item);
        }

        order = new Order(0, LocalDate.now(), OrderStatus.PENDING, dtOrder.getType(), items);
        orderRepository.save(order);
    }
}
