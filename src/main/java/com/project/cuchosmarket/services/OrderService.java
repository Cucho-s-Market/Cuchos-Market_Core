package com.project.cuchosmarket.services;

import com.project.cuchosmarket.dto.DtOrder;
import com.project.cuchosmarket.dto.DtItem;
import com.project.cuchosmarket.dto.DtOrder;
import com.project.cuchosmarket.enums.OrderStatus;
import com.project.cuchosmarket.exceptions.EmployeeNotWorksInException;
import com.project.cuchosmarket.exceptions.OrderNotExistException;
import com.project.cuchosmarket.models.Branch;
import com.project.cuchosmarket.models.Order;
import com.project.cuchosmarket.models.User;
import com.project.cuchosmarket.repositories.EmployeeRepository;
import com.project.cuchosmarket.repositories.OrderRepository;
import com.project.cuchosmarket.repositories.UserRepository;
import com.project.cuchosmarket.exceptions.*;
import com.project.cuchosmarket.models.*;
import com.project.cuchosmarket.repositories.*;
import com.project.cuchosmarket.repositories.specifications.OrderSpecifications;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
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

    private void employeeWorksInBranch(String username, Long marketBranchId) throws EmployeeNotWorksInException {
        User user = userRepository.findByEmail(username);
        Branch branchEmployee = employeeRepository.findById(user.getId()).get().getBranch();

        if (!branchEmployee.getId().equals(marketBranchId)) throw new EmployeeNotWorksInException();
    }

    public List<Order> getOrdersBy(String userEmail, Long marketBranchId, String orderStatus, LocalDate startDate,
                                   LocalDate endDate, String orderDirection) throws EmployeeNotWorksInException, UserNotExistException {
        employeeWorksInBranch(userEmail, marketBranchId);

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
    public void buyProducts(String userEmail, DtOrder dtOrder) throws BranchNotExistException, UserNotExistException, ProductNotExistException, NoStockException {
        User user = userRepository.findByEmail(userEmail);
        Customer customer = customerRepository.findById(user.getId()).orElseThrow(UserNotExistException::new);
        Branch marketBranch = marketBranchRepository.findById(dtOrder.getBranchId())
                .orElseThrow(() -> new BranchNotExistException(dtOrder.getBranchId()));

        if (dtOrder.getProducts().isEmpty()) {
            throw new IllegalArgumentException("No se selecciono ningun producto para comprar.");
        }

        List<Item> items = new ArrayList<>();
        Order order = null;
        float totalPrice = 0;

        for (DtItem dtItem : dtOrder.getProducts()) {
            if (dtItem.getQuantity() < 1) throw new IllegalArgumentException("La cantidad del producto que se encarga ha de ser mayor a 0.");

            Product product = productRepository.findById(dtItem.getName()).orElseThrow(() -> new ProductNotExistException(dtItem.getName()));
            Stock productStock = stockRepository.findById(new StockId(product, marketBranch)).get();

            if (productStock.getQuantity() < 1 || productStock.getQuantity() < dtItem.getQuantity()) {
                throw new NoStockException("Stock insuficiente para " + dtItem.getName() + " en sucursal.");
            } else {
                productStock.setQuantity(productStock.getQuantity() - dtItem.getQuantity());
            }

            Item item = new Item(dtItem.getName(), product.getPrice(), product.getPrice() * dtItem.getQuantity(),
                    dtItem.getQuantity(), product);
            items.add(item);
            totalPrice+=item.getFinalPrice();
        }

        order = new Order(totalPrice, LocalDate.now(), OrderStatus.PENDING, dtOrder.getType(), items);
        orderRepository.save(order);

        customer.addOrder(order);
        customerRepository.save(customer);

        marketBranch.addOrder(order);
        marketBranchRepository.save(marketBranch);
    }

    public void updateStatus(String userEmail, DtOrder dtOrder) throws EmployeeNotWorksInException, OrderNotExistException {
        employeeWorksInBranch(userEmail, dtOrder.getBranchId());

        Order order = orderRepository.findById(dtOrder.getId()).orElseThrow(() -> new OrderNotExistException(dtOrder.getId()));

        if (order.getStatus().equals(OrderStatus.CANCELLED)) throw new IllegalArgumentException("Orden ya cancelada.");

        order.setStatus(dtOrder.getStatus());
        orderRepository.save(order);
    }
}
