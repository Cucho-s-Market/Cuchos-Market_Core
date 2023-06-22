package com.project.cuchosmarket;
import com.project.cuchosmarket.dto.DtOrder;
import com.project.cuchosmarket.enums.OrderStatus;
import com.project.cuchosmarket.exceptions.*;
import com.project.cuchosmarket.models.*;
import com.project.cuchosmarket.repositories.*;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.project.cuchosmarket.models.Branch;
import com.project.cuchosmarket.models.Order;
import com.project.cuchosmarket.models.StockId;
import com.project.cuchosmarket.models.User;
import com.project.cuchosmarket.services.OrderService;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderServiceTest {
    @MockBean
    private OrderRepository orderRepository;
    @Autowired
    private OrderService orderService;
    @MockBean
    private BranchRepository branchRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private StockRepository stockRepository;

    @MockBean
    private PromotionRepository promotionRepository;

    @MockBean
    private EmployeeRepository employeeRepository;

    @MockBean
    private AddressNotExistException addressNotExistExceptionMock;

        @Test
        public void test(){
            // Configuración
            User user = new Customer();
            String userEmail = "example@example.com";
            user.setEmail(userEmail);
            when(userRepository.findByEmail(userEmail)).thenReturn(user);

            Branch branch = new Branch();
            Long branchId = 123L;
            branch.setId(branchId);
            Long addressId = 456L;
            DtOrder dtOrder = new DtOrder();

            Product product = new Product();
            String productCode = "1L";
            product.setCode(productCode);
            Stock stock = new Stock();
            Address address = new Address();
            address.setId(addressId);

            when(branchRepository.findById(branchId)).thenReturn(Optional.of(branch));
            when(productRepository.findById(anyString())).thenReturn(Optional.of(product));
            when(stockRepository.findById(any(StockId.class))).thenReturn(Optional.of(stock));

            try {
                orderService.buyProducts(userEmail,dtOrder);
            } catch (BranchNotExistException e) {
                throw new RuntimeException(e);
            } catch (UserNotExistException e) {
                throw new RuntimeException(e);
            } catch (ProductNotExistException e) {
                throw new RuntimeException(e);
            } catch (NoStockException e) {
                throw new RuntimeException(e);
            } catch (InvalidOrderException e) {
                throw new RuntimeException(e);
            } catch (AddressNotExistException e) {
                throw new RuntimeException(e);
            }

        }
    @SneakyThrows
    @Test
    public void testGetOrder() {
        Long orderId = 1l;
        Order order = new Order();
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        order = orderService.getOrder(orderId);
        System.out.println(order);
        verify(orderRepository, atLeastOnce()).findById(orderId);

    }

    @SneakyThrows
    @Test
    public void testGetPurchasesByCustomer(){
        String userEmail = "adri@email.com";
        int pageNumber = 2;
        int pageSize = 3;
        String orderStatus = "PENDING";
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(3);
        String orderDirection = "calle 1234";
        User user = new Customer();
        when(userRepository.findByEmail(userEmail)).thenReturn(user);

        Page<DtOrder> salida = null;

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("creationDate",orderDirection));
        when(orderRepository.findCustomerOrders(user.getId(), OrderStatus.PENDING, startDate, endDate, pageable)).thenReturn(salida);
        salida = orderService.getPurchasesByCustomer("adri@email.com",2,3,"PENDING",LocalDate.now(),LocalDate.now().plusDays(3),"calle 1234");
        System.out.println(salida.get());
    }

    @SneakyThrows
    @Test
    public void testGetOrdersBy(){
        String userEmail = "adri@email.com";
        int pageNumber = 2;
        int pageSize = 3;
        String orderStatus = "PENDING";
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(3);
        String orderDirection = "calle 1234";

        User user = new Customer("adriana", "pisano", userEmail, "123", LocalDate.of(1988,10,01),99867791, 44017427 );
        when(userRepository.findByEmail(userEmail)).thenReturn(user);

        Long marketBranchId = 1l;

        Page<DtOrder> salida = null;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("o.creationDate", orderDirection));

        when(branchRepository.findOrders(marketBranchId, OrderStatus.PENDING, startDate, endDate, pageable)).thenReturn(salida);
        salida = orderService.getOrdersBy(userEmail,pageNumber,pageSize,marketBranchId,orderStatus,startDate, endDate, orderDirection);
        System.out.println(salida.getTotalElements());
    }
@Test
    public void testUpdateStatus() throws EmployeeNotWorksInException, OrderNotExistException, InvalidOrderException {
        String userEmail = "adri@email.com";
        DtOrder dtOrder = new DtOrder();
        dtOrder.setId(1l);
        dtOrder.setStatus(OrderStatus.PENDING);
      //  User user = new Customer("adriana", "pisano", userEmail, "123", LocalDate.of(1988,10,01),99867791, 44017427 );

        Order order = new Order();
        order.setId(1l);
        when(orderRepository.save(order));
        when(orderRepository.existsById(1l)).thenReturn(true);

        orderService.updateStatus(userEmail,dtOrder);
    }
    @Test
    public void cancelOrder() {
        String userEmail = "adri@email.com";
        Long order_id = 1l;
        Customer user = new Customer() ;
        user.setId(11l);

        when(userRepository.findByEmail(userEmail)).thenReturn(user);

        Order order = new Order();
        order.setId(order_id);
        order.setCustomer(user);
        order.setStatus(OrderStatus.PENDING);
        when(orderRepository.save(order));
        try {
            orderService.cancelOrder(userEmail,order_id);
        } catch (OrderNotExistException e) {
            throw new RuntimeException(e);
        } catch (InvalidOrderException e) {
            throw new RuntimeException(e);
        }
    }
}
