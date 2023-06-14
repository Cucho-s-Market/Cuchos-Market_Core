package com.project.cuchosmarket.services;

import com.project.cuchosmarket.dto.DtItem;
import com.project.cuchosmarket.dto.DtOrder;
import com.project.cuchosmarket.enums.OrderStatus;
import com.project.cuchosmarket.enums.OrderType;
import com.project.cuchosmarket.exceptions.*;
import com.project.cuchosmarket.models.*;
import com.project.cuchosmarket.repositories.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final BranchRepository branchRepository;
    private final ProductRepository productRepository;
    private final StockRepository stockRepository;

    private void employeeWorksInBranch(String username, Long marketBranchId) throws EmployeeNotWorksInException {
        User user = userRepository.findByEmail(username);
        Branch branchEmployee = employeeRepository.findById(user.getId()).get().getBranch();

        if (!branchEmployee.getId().equals(marketBranchId)) throw new EmployeeNotWorksInException();
    }

    public Page<DtOrder> getOrdersBy(String userEmail, int pageNumber, int pageSize, Long marketBranchId, String orderStatus, LocalDate startDate,
                                     LocalDate endDate, String orderDirection) throws EmployeeNotWorksInException, UserNotExistException, InvalidOrderException {
        employeeWorksInBranch(userEmail, marketBranchId);

        OrderStatus status = null;
        Sort sort;
        if (orderStatus != null) {
            try {
                status = OrderStatus.valueOf(orderStatus.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new InvalidOrderException("Estado de la compra invalido.");
            }
        }
        if (orderDirection == null || orderDirection.equalsIgnoreCase("desc")) sort = Sort.by("o.creationDate").descending();
        else sort = Sort.by("o.creationDate").ascending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        return branchRepository.findOrders(marketBranchId, status, startDate, endDate,  pageable);
    }

    public Order getOrder(Long orderId) throws OrderNotExistException {
        return orderRepository.findById(orderId).orElseThrow(() -> new OrderNotExistException(orderId));
    }

    @Transactional
    public void buyProducts(String userEmail, DtOrder dtOrder) throws BranchNotExistException, UserNotExistException, ProductNotExistException, NoStockException, InvalidOrderException, AddressNotExistException {
        User user = userRepository.findByEmail(userEmail);
        Customer customer = customerRepository.findById(user.getId()).orElseThrow(UserNotExistException::new);
        Branch marketBranch = branchRepository.findById(dtOrder.getBranchId())
                .orElseThrow(() -> new BranchNotExistException(dtOrder.getBranchId()));

        if (dtOrder.getStatus() == null || dtOrder.getType() == null) throw new InvalidOrderException("Informacion de orden invalida.");
        if (dtOrder.getProducts().isEmpty()) throw new InvalidOrderException("No se selecciono ningun producto para comprar.");


        List<Item> items = new ArrayList<>();
        Order order = null;
        float totalPrice = 0;

        for (DtItem dtItem : dtOrder.getProducts()) {
            if (dtItem.getQuantity() < 1) throw new InvalidOrderException("La cantidad del producto que se encarga ha de ser mayor a 0.");

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
        if (order.getType().equals(OrderType.DELIVERY)) {

            if (dtOrder.getAddressId() == null) throw new IllegalArgumentException("No se ha seleccionado ninguna direccion.");
            Address address = customer.getAddresses()
                    .stream()
                    .filter(address1 -> address1.getId().equals(dtOrder.getAddressId()))
                    .findFirst()
                    .orElseThrow(AddressNotExistException::new);
            order.setClientAddress(address);
        }

        orderRepository.save(order);

        customer.addOrder(order);
        customerRepository.save(customer);

        marketBranch.addOrder(order);
        branchRepository.save(marketBranch);
    }

    private Order validateOrder(Long orderId) throws OrderNotExistException, InvalidOrderException {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotExistException(orderId));

        if (order.getStatus().equals(OrderStatus.CANCELLED)) throw new InvalidOrderException("Orden ya cancelada.");
        if (order.getStatus().equals(OrderStatus.DELIVERED)) throw new InvalidOrderException("Orden ya entregada.");

        return order;
    }

    public void updateStatus(String userEmail, DtOrder dtOrder) throws EmployeeNotWorksInException, OrderNotExistException, InvalidOrderException {
        employeeWorksInBranch(userEmail, dtOrder.getBranchId());

        Order order = validateOrder(dtOrder.getId());
        if (dtOrder.getStatus().equals(OrderStatus.CANCELLED) || dtOrder.getStatus().equals(OrderStatus.DELIVERED)) order.setEndDate(LocalDate.now());

        order.setStatus(dtOrder.getStatus());
        orderRepository.save(order);
    }

    public void cancelOrder(String userEmail, Long order_id) throws OrderNotExistException, InvalidOrderException {
        Customer customer = (Customer) userRepository.findByEmail(userEmail);
        if (customer.getOrder(order_id) == null) throw new InvalidOrderException("Este pedido no pertence al cliente.");

        Order order = validateOrder(order_id);
        if (order.getStatus().equals(OrderStatus.PENDING)) order.setStatus(OrderStatus.CANCELLED);
        else throw new InvalidOrderException("La orden " + order_id + " no se puede cancelar. Verifique estado de compra.");

        orderRepository.save(order);
    }
}
