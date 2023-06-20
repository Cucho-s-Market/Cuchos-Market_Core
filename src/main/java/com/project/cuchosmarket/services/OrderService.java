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
    private final BranchRepository branchRepository;
    private final ProductRepository productRepository;
    private final StockRepository stockRepository;
    private final PromotionRepository promotionRepository;

    private void employeeWorksInBranch(String username, Long marketBranchId) throws EmployeeNotWorksInException {
        User user = userRepository.findByEmail(username);
        Branch branchEmployee = employeeRepository.findById(user.getId()).get().getBranch();

        if (!branchEmployee.getId().equals(marketBranchId)) throw new EmployeeNotWorksInException();
    }

    private OrderStatus parseOrderStatus(String orderStatus) throws InvalidOrderException {
        if (orderStatus == null) return null;

        try {
            return OrderStatus.valueOf(orderStatus.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidOrderException("Estado de la compra inválido.");
        }
    }
    private Sort createSortBy(String sortBy, String orderDirection) {
        return (orderDirection == null || orderDirection.equalsIgnoreCase("desc")) ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();
    }

    public Page<DtOrder> getOrdersBy(String userEmail, int pageNumber, int pageSize, Long marketBranchId, String orderStatus, LocalDate startDate,
                                     LocalDate endDate, String orderDirection) throws EmployeeNotWorksInException, UserNotExistException, InvalidOrderException {
        employeeWorksInBranch(userEmail, marketBranchId);

        OrderStatus status = parseOrderStatus(orderStatus);
        Sort sort = createSortBy("o.creationDate", orderDirection);

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        return branchRepository.findOrders(marketBranchId, status, startDate, endDate, pageable);
    }

    public Page<DtOrder> getPurchasesByCustomer(String userEmail, int pageNumber, int pageSize, String orderStatus, LocalDate startDate,
                                                LocalDate endDate, String orderDirection) throws UserNotExistException, InvalidOrderException {
        User user = userRepository.findByEmail(userEmail);
        if (user == null) throw new UserNotExistException();

        OrderStatus status = parseOrderStatus(orderStatus);
        Sort sort = createSortBy("creationDate", orderDirection);

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        return orderRepository.findCustomerOrders(user.getId(), status, startDate, endDate, pageable);
    }

    public Order getOrder(Long orderId) throws OrderNotExistException {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotExistException(orderId));
        order.getCustomer().setId(null);
        order.getCustomer().setPassword(null);
        order.getCustomer().setRole(null);
        order.getCustomer().setDisabled(null);
        order.getCustomer().setAddresses(null);

        return order;
    }

    @Transactional
    public void buyProducts(String userEmail, DtOrder dtOrder) throws BranchNotExistException, UserNotExistException, ProductNotExistException, NoStockException, InvalidOrderException, AddressNotExistException {
        User user = userRepository.findByEmail(userEmail);
        if (user == null) throw new UserNotExistException();

        Customer customer = (Customer) user;
        Branch branch = branchRepository.findById(dtOrder.getBranchId())
                .orElseThrow(() -> new BranchNotExistException(dtOrder.getBranchId()));

        if (dtOrder.getStatus() == null && dtOrder.getType() == null) throw new InvalidOrderException("Informacion de orden invalida.");
        if (dtOrder.getProducts().isEmpty()) throw new InvalidOrderException("No se selecciono ningun producto para comprar.");


        List<Item> items = new ArrayList<>();
        Order order;
        float totalPrice = 0;

        for (DtItem dtItem : dtOrder.getProducts()) {
            if (dtItem.getQuantity() < 1) throw new InvalidOrderException("La cantidad del producto que se encarga ha de ser mayor a 0.");

            Product product = productRepository.findById(dtItem.getName()).orElseThrow(() -> new ProductNotExistException(dtItem.getName()));
            Stock productStock = stockRepository.findById(new StockId(product, branch)).get();

            if (productStock.getQuantity() < 1 || productStock.getQuantity() < dtItem.getQuantity()) {
                throw new NoStockException("Stock insuficiente para " + dtItem.getName() + " en sucursal.");
            } else {
                productStock.setQuantity(productStock.getQuantity() - dtItem.getQuantity());
            }

            Item item = new Item(dtItem.getName(), product.getPrice(), applyPromotion(product, dtItem.getQuantity()),
                    dtItem.getQuantity(), product);
            items.add(item);
            totalPrice+=item.getFinalPrice();
        }

        order = new Order(totalPrice, LocalDate.now(), OrderStatus.PENDING, dtOrder.getType(), items);

        if (order.getType().equals(OrderType.DELIVERY)) {
            if (dtOrder.getAddressId() == null) throw new InvalidOrderException("No se ha seleccionado ninguna direccion.");
            Address address = customer.getAddresses()
                    .stream()
                    .filter(address1 -> address1.getId().equals(dtOrder.getAddressId()))
                    .findFirst()
                    .orElseThrow(AddressNotExistException::new);
            order.setClientAddress(address.toString());
        }

        order.setCustomer(customer);
        orderRepository.save(order);

        branch.addOrder(order);
        branchRepository.save(branch);
    }

    private float applyPromotion(Product product, int productQuantity) {
        List<Promotion> productPromotions = promotionRepository.findPromotionsByProduct(product);
        if (productPromotions.isEmpty()) return product.getPrice();

        float finalPrice = 0f;
        for (Promotion promotion : productPromotions) {
            if (promotion instanceof Discount discount) {
                float totalCost = product.getPrice() * productQuantity;
                float discountAmount = totalCost * (discount.getPercentage() / 100.0f);
                finalPrice += totalCost - discountAmount;
            } else if (promotion instanceof NxM nxM) {
                int paidProducts = productQuantity / nxM.getN();
                int notApply = productQuantity % nxM.getN();

                finalPrice = (paidProducts * product.getPrice() * nxM.getM()) + (notApply * product.getPrice());
            }
        }

        return finalPrice;
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
        Order order = validateOrder(order_id);

        if(!order.getCustomer().getId().equals(customer.getId())) throw new InvalidOrderException("Este pedido no pertence al cliente.");

        if (order.getStatus().equals(OrderStatus.PENDING)) {
            order.setStatus(OrderStatus.CANCELLED);
            order.setEndDate(LocalDate.now());
        }
        else throw new InvalidOrderException("La orden " + order_id + " no se puede cancelar. Verifique estado de compra.");

        orderRepository.save(order);
    }

    public void statusAfterPayment(Long orderId, DtOrder dtOrder) throws InvalidOrderException, OrderNotExistException {
        Order order = validateOrder(orderId);

        if (dtOrder.getStatus().equals(OrderStatus.CANCELLED)) {
            order.setStatus(OrderStatus.CANCELLED);
            order.setEndDate(LocalDate.now());
            orderRepository.save(order);
        } else System.out.println("Se envia email al cliente.");
    }
}
